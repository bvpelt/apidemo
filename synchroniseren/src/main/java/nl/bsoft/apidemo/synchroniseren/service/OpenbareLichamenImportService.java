package nl.bsoft.apidemo.synchroniseren.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.mapper.OpenbaarLichaamMapper;
import nl.bsoft.apidemo.library.mapper.OpenbaarLichaamMapperImpl;
import nl.bsoft.apidemo.library.model.dto.AuditLogDto;
import nl.bsoft.apidemo.library.model.dto.OpenbaarLichaamDto;
import nl.bsoft.apidemo.library.service.APIService;
import nl.bsoft.apidemo.library.service.AuditLogStorageService;
import nl.bsoft.apidemo.library.service.OpenbaarLichaamStorageService;
import nl.bsoft.apidemo.synchroniseren.util.TaskSemaphore;
import nl.bsoft.bestuurlijkegrenzen.generated.model.OpenbaarLichaam;
import nl.bsoft.bestuurlijkegrenzen.generated.model.OpenbareLichamenGet200Response;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenbareLichamenImportService extends ImportService {
    //private static final int MAX_PAGE_SIZE = 50;
    private final OpenbaarLichaamStorageService openbaarLichaamStorageService;
    private final AuditLogStorageService auditLogStorageService;
    private final OpenbaarLichaamMapper openbaarLichaamMapper = new OpenbaarLichaamMapperImpl();
    private final APIService APIService;
    private final TaskSemaphore taskSemaphore;

    @Async
    public UpdateCounter getAllOpenbareLichamen(LocalDate validAt) {
        int page = 1;
        UpdateCounter counter = new UpdateCounter();
        boolean morePages = true;

        log.info("Start synchronizing openbarelichamen");

        if (taskSemaphore.getAndSetFree(false)) {
            log.info("Start synchronizing openbarelichamen, locked task");

            LocalDateTime jobstart = LocalDateTime.now();
            String jobName = "openbarelichamen";
            String result = "SUCCESS";
            AuditLogDto auditLog = createAuditLog(jobName, jobstart, validAt, JobStatus.START.name());

            auditLog = auditLogStorageService.Save(auditLog);

            try {
                while (morePages) {
                    OpenbareLichamenGet200Response openbareLichamenGet200Response = getOpenbaarLichaamPage(page, MAX_PAGE_SIZE, validAt);

                    if (openbareLichamenGet200Response != null) {
                        if (openbareLichamenGet200Response.getEmbedded() != null) {
                            List<OpenbaarLichaam> openbaarLichaamList = openbareLichamenGet200Response.getEmbedded().getOpenbareLichamen();
                            log.debug("Retrieved page: {}", page);
                            if ((openbaarLichaamList != null) && (openbaarLichaamList.size() > 0)) {
                                processOpenbareLichamen(counter, openbaarLichaamList);
                            } else {
                                log.debug("No results in the list");
                            }
                            if (openbareLichamenGet200Response.getLinks().getNext() != null) {
                                page++;
                            } else {
                                log.debug("No more pages available");
                                morePages = false;
                            }
                        } else {
                            morePages = false;
                            log.error("No embedded openbare lichamen");
                        }
                    } else {
                        morePages = false;
                    }
                }
            } catch (Exception ex) {
                log.error("Error while updating page: {} with parameter validAt: {} message: {}", page, validAt, ex.toString());
                result = ex.getMessage();
            }
            AuditLogDto auditLogDtoEnd = createAuditLogEnd(auditLog, JobStatus.FINISHED.name(), counter, result);
            auditLogDtoEnd = auditLogStorageService.Save(auditLogDtoEnd);
            taskSemaphore.getAndSetFree(true);
            log.info("End   synchronizing openbarelichamen, released locked task");
        } else {
            log.info("Start synchronizing openbarelichamen, no lock skipped task");
        }
        log.info("End   synchronizing bestuurlijkegebieden");

        return counter;
    }

    public OpenbareLichamenGet200Response getOpenbaarLichaamPage(Integer page, Integer size, LocalDate validAt) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/openbare-lichamen");

        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
        uriComponentsBuilder.queryParam("geldigOp", validAt.toString());
        log.debug("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), OpenbareLichamenGet200Response.class);
    }

    private int processOpenbareLichamen(UpdateCounter counter, List<OpenbaarLichaam> openbareLichamen) {

        openbareLichamen.stream().forEach(
                (openbaarLichaam -> {
                    processOpenbareLichaam(counter, openbaarLichaam);
                })
        );
        return openbareLichamen.size();
    }

    private OpenbaarLichaamDto toDto(OpenbaarLichaam openbaarLichaam) {
        OpenbaarLichaamDto bestemming = null;
        try {
            bestemming = openbaarLichaamMapper.toOpenbaarLichaamDto(openbaarLichaam);
        } catch (Exception e) {
            log.error("Error mapping bestuurlijkgebied: {}", e); // skip, log error and continue
        }

        return bestemming;
    }

    private OpenbaarLichaamDto CopyToDto(OpenbaarLichaamDto bron, LocalDateTime registratieMoment) {
        OpenbaarLichaamDto bestemming = new OpenbaarLichaamDto();

        bestemming.setType(bron.getType());
        bestemming.setBeginRegistratie(registratieMoment);
        bestemming.setCode(bron.getCode());
        bestemming.setNaam(bron.getNaam());
        bestemming.setBestuurslaag(bron.getBestuurslaag());
        bestemming.setOin(bron.getOin());

        return bestemming;
    }

    private void processOpenbareLichaam(UpdateCounter counter, OpenbaarLichaam openbaarLichaam) {
        log.debug("Openbaarlichaam - code: {}, oin: {}, type: {}, naam: {}, bestuurslaag: {}",
                openbaarLichaam.getCode().isPresent() ? openbaarLichaam.getCode().get() : "",
                openbaarLichaam.getOin().isPresent() ? openbaarLichaam.getOin().get() : "",
                openbaarLichaam.getType().getValue(),
                openbaarLichaam.getNaam(),
                openbaarLichaam.getBestuurslaag().getValue());

        // Check if there is already a known openbaarlichaam
        List<OpenbaarLichaamDto> openbaarLichaamDtoList = openbaarLichaamStorageService.findByIdentificatie(openbaarLichaam.getCode().get());
        int size = openbaarLichaamDtoList.size();
        if (size == 0) { // no entries found
            log.debug("Create and store new entry for code: {}", openbaarLichaam.getCode().get());
            OpenbaarLichaamDto openbaarLichaamDto = toDto(openbaarLichaam);
            LocalDateTime registrationMoment = LocalDateTime.now();
            openbaarLichaamDto.setBeginRegistratie(registrationMoment);

            openbaarLichaamStorageService.Save(openbaarLichaamDto);
            counter.add();
        } else {
            if (size == 1) { // exactly 1 entrie found, update
                addNewEntry(openbaarLichaam, openbaarLichaamDtoList, counter);
            } else { // size > 1 order is beginregistratie desc, begingeldigheid desc so first entry must be used as last entry
                log.info("Found {} hits for identificatie: {}", size, openbaarLichaam.getCode().get());
                addNewEntry(openbaarLichaam, openbaarLichaamDtoList, counter);
            }
        }
    }

    private void addNewEntry(OpenbaarLichaam openbaarLichaam, List<OpenbaarLichaamDto> openbaarLichaamDtoList, UpdateCounter counter) {
        if (!compairOpenbaarLichaam(openbaarLichaam, openbaarLichaamDtoList.get(0))) {
            log.debug("Update and store entry for code: {}", openbaarLichaam.getCode().get());

            LocalDateTime registrationMoment = LocalDateTime.now();

            OpenbaarLichaamDto currentDto = openbaarLichaamDtoList.get(0);
            //OpenbaarLichaamDto copyDto = CopyToDto(currentDto, registrationMoment);
            OpenbaarLichaamDto lastDto = toDto(openbaarLichaam);

            // update current eindregistratie
            currentDto.setEindRegistratie(registrationMoment);
            lastDto.setBeginRegistratie(registrationMoment);
            // save historie
            openbaarLichaamStorageService.SaveWithHistory(currentDto, lastDto);
            counter.updated();
        } else {
            log.debug("Identical entry for code: {}", openbaarLichaam.getCode().get());
            counter.unmodified();
        }
    }

    private boolean compairOpenbaarLichaam(OpenbaarLichaam openbaarLichaam, OpenbaarLichaamDto openbaarLichaamDto) {
        boolean equal = true;

        equal = openbaarLichaam.getCode().get().equals(openbaarLichaamDto.getCode());
        if (equal) {
            if (openbaarLichaam.getOin().isPresent()) {
                equal = openbaarLichaam.getOin().get().equals(openbaarLichaamDto.getOin());
            }
        } else {
            log.debug("code gewijzigd api: {} database: {}", openbaarLichaam.getCode().get(), openbaarLichaamDto.getCode());
            return equal;
        }

        if (equal) {
            equal = openbaarLichaam.getType().getValue().equals(openbaarLichaamDto.getType());
        } else {
            if (openbaarLichaam.getOin().isPresent()) {
                log.debug("oin gewijzigd api: {} database: {}", openbaarLichaam.getOin().get(), openbaarLichaamDto.getOin());
            }
            return equal;
        }

        if (equal) {
            equal = openbaarLichaam.getNaam().equals(openbaarLichaamDto.getNaam());
        } else {
            log.debug("type gewijzigd api: {} database: {}", openbaarLichaam.getType().getValue(), openbaarLichaamDto.getType());
            return equal;
        }

        if (equal) {
            equal = openbaarLichaam.getBestuurslaag().getValue().equals(openbaarLichaamDto.getBestuurslaag());
        }

        if (!equal) {
            log.debug("bestuurslaag gewijzigd api: {}, database: {}", openbaarLichaam.getBestuurslaag().getValue(), openbaarLichaamDto.getBestuurslaag());
        }

        return equal;
    }
}

