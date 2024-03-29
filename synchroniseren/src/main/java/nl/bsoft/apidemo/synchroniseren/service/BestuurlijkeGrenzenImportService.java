package nl.bsoft.apidemo.synchroniseren.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.mapper.BestuurlijkgebiedMapper;
import nl.bsoft.apidemo.library.mapper.LocatieMapper;
import nl.bsoft.apidemo.library.model.dto.AuditLogDto;
import nl.bsoft.apidemo.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.apidemo.library.model.dto.LocatieDto;
import nl.bsoft.apidemo.library.service.APIService;
import nl.bsoft.apidemo.library.service.AuditLogStorageService;
import nl.bsoft.apidemo.library.service.BestuurlijkeGebiedenStorageService;
import nl.bsoft.apidemo.library.service.LocatieStorageService;
import nl.bsoft.apidemo.synchroniseren.SynchBestuurlijkgebied;
import nl.bsoft.apidemo.synchroniseren.util.TaskSemaphore;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkGebied;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkeGebiedenGet200Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BestuurlijkeGrenzenImportService extends ImportService {
    // private static final int MAX_PAGE_SIZE = 50;
    private final APIService APIService;
    private final BestuurlijkeGebiedenStorageService bestuurlijkeGebiedenStorageService;
    private final LocatieStorageService locatieStorageService;
    private final AuditLogStorageService auditLogStorageService;
    private final BestuurlijkgebiedMapper bestuurlijkgebiedMapper;
    private final LocatieMapper locatieMapper;
    private final TaskSemaphore taskSemaphore;

    @Async
    public UpdateCounter getAllBestuurlijkebebieden(LocalDate validAt) {
        int page = 1;
        UpdateCounter counter = new UpdateCounter();

        log.info("Start synchronizing bestuurlijkegebieden");

        if (taskSemaphore.getAndSetFree(false)) {
            log.info("Start synchronizing bestuurlijkegebieden, locked task");

            LocalDateTime jobstart = LocalDateTime.now();
            String jobName = "bestuurlijkegebieden";

            AuditLogDto auditLog = createAuditLog(jobName, jobstart, validAt, JobStatus.START.name());
            String result = "SUCCESS";
            auditLog = auditLogStorageService.Save(auditLog);

            try {
                boolean morePages = true;
                while (morePages) {
                    BestuurlijkeGebiedenGet200Response bestuurlijkeGebiedenGet200Response = getBestuurlijkGebiedPage(page, MAX_PAGE_SIZE, validAt);
                    if (bestuurlijkeGebiedenGet200Response != null) {
                        if (bestuurlijkeGebiedenGet200Response.getEmbedded() != null) {
                            List<BestuurlijkGebied> bestuurlijkGebiedList = bestuurlijkeGebiedenGet200Response.getEmbedded().getBestuurlijkeGebieden();
                            log.debug("Retrieved page: {}", page);
                            if ((bestuurlijkGebiedList != null) && (bestuurlijkGebiedList.size() > 0)) {
                                processBestuurlijkeGrenzen(counter, bestuurlijkGebiedList, jobstart);
                            } else {
                                log.debug("No results in the list");
                            }
                            if (bestuurlijkeGebiedenGet200Response.getLinks().getNext() != null) {
                                page++;
                            } else {
                                log.debug("No more pages available");
                                morePages = false;
                            }
                        } else {
                            morePages = false;
                            log.error("No embedded bestuurlijke gebieden");
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
            log.info("End   synchronizing bestuurlijkegebieden, released locked task");
        } else {
            log.info("Start synchronizing bestuurlijkegebieden, no lock skipped task");
        }
        log.info("End   synchronizing bestuurlijkegebieden");

        return counter;
    }

    private int processBestuurlijkeGrenzen(UpdateCounter counter, List<BestuurlijkGebied> bestuurlijkeGebieden, LocalDateTime timestamp) {

        bestuurlijkeGebieden.stream().forEach(
                (bestuurlijkGebied -> {
                    procesBestuurlijkeGrens(counter, bestuurlijkGebied, timestamp);
                })
        );
        return bestuurlijkeGebieden.size();
    }

    private boolean compairBestuurlijkgebied(BestuurlijkGebied bestuurlijkGebied, BestuurlijkGebiedDto bestuurlijkGebiedDto) {
        boolean equal = true;

        equal = bestuurlijkGebied.getIdentificatie().equals(bestuurlijkGebiedDto.getIdentificatie());
        if (equal) {
            equal = bestuurlijkGebied.getType().getValue().equals(bestuurlijkGebiedDto.getType());
        } else {
            log.debug("identificatie gewijzigd api: {} database: {}", bestuurlijkGebied.getIdentificatie(), bestuurlijkGebiedDto.getIdentificatie());
            return equal;
        }

        if (equal) {
            equal = bestuurlijkGebied.getDomein().equals(bestuurlijkGebiedDto.getDomein());
        } else {
            log.debug("type gewijzigd api: {} database: {}", bestuurlijkGebied.getType(), bestuurlijkGebiedDto.getType());
            return equal;
        }

        if (equal) {
            equal = bestuurlijkGebied.getEmbedded().getMetadata().getBeginGeldigheid().get().equals(bestuurlijkGebiedDto.getBeginGeldigheid());
        } else {
            log.debug("domein gewijzigd api: {} database: {}", bestuurlijkGebied.getDomein(), bestuurlijkGebiedDto.getDomein());
            return equal;
        }

        if (equal) {
            if (bestuurlijkGebied.getEmbedded().getMetadata().getEindGeldigheid().isPresent()) {
                equal = bestuurlijkGebied.getEmbedded().getMetadata().getEindGeldigheid().get().equals(bestuurlijkGebiedDto.getEindGeldigheid());
            }
        } else {
            log.debug("beginGeldigheid gewijzigd api: {} database: {}", bestuurlijkGebied.getEmbedded().getMetadata().getBeginGeldigheid().get(), bestuurlijkGebiedDto.getBeginGeldigheid());
            return equal;
        }

        if (equal) {
            equal = DigestUtils.md5Hex(bestuurlijkGebied.getGeometrie().toString().toUpperCase()).equals(bestuurlijkGebiedDto.getMd5hash());
        } else {
            if (bestuurlijkGebied.getEmbedded().getMetadata().getEindGeldigheid().isPresent()) {
                log.debug("eindGeldigheid gewijzigd api: {} database: {}", bestuurlijkGebied.getEmbedded().getMetadata().getEindGeldigheid().get(), bestuurlijkGebiedDto.getEindGeldigheid());
            }
            return equal;
        }

        if (!equal) {
            log.debug("geometrie md5hash and content gewijzigd api: {} database: {}", DigestUtils.md5Hex(bestuurlijkGebied.getGeometrie().toString().toUpperCase()), bestuurlijkGebiedDto.getMd5hash());
        }

        return equal;
    }

    private BestuurlijkGebiedDto toDto(BestuurlijkGebied bestuurlijkGebied) {
        BestuurlijkGebiedDto bestemming = null;
        try {
            bestemming = bestuurlijkgebiedMapper.toBestuurlijkgeBiedDto(bestuurlijkGebied);
        } catch (Exception e) {
            log.error("Error mapping bestuurlijkgebied: {}", e); // skip, log error and continue
        }

        return bestemming;
    }

    private SynchBestuurlijkgebied toSyncDto(BestuurlijkGebied bestuurlijkGebied) {
        BestuurlijkGebiedDto bestemming = null;
        LocatieDto locatieDto = null;

        String md5hash = DigestUtils.md5Hex(bestuurlijkGebied.getGeometrie().toString().toUpperCase());

        try {
            bestemming = bestuurlijkgebiedMapper.toBestuurlijkgeBiedDto(bestuurlijkGebied);
            bestemming.setMd5hash(md5hash);
            locatieDto = locatieMapper.toLocatieDto(bestuurlijkGebied);
            locatieDto.setMd5hash(md5hash);
        } catch (Exception e) {
            log.error("Error mapping bestuurlijkgebied: {}", e); // skip, log error and continue
        }

        SynchBestuurlijkgebied synchBestuurlijkgebied = new SynchBestuurlijkgebied();
        synchBestuurlijkgebied.setBestuurlijkGebiedDto(bestemming);
        synchBestuurlijkgebied.setLocatieDto(locatieDto);

        return synchBestuurlijkgebied;
    }

    private BestuurlijkGebiedDto copyToDto(BestuurlijkGebiedDto bron, LocalDateTime timeStamp) {
        BestuurlijkGebiedDto bestemming = new BestuurlijkGebiedDto();
        bestemming.setIdentificatie(bron.getIdentificatie());
        bestemming.setDomein(bron.getDomein());
        bestemming.setType(bron.getType());
        bestemming.setMd5hash(bron.getMd5hash());
        bestemming.setBeginGeldigheid(bron.getBeginGeldigheid());
        bestemming.setEindGeldigheid(bron.getEindGeldigheid());
        bestemming.setBeginRegistratie(timeStamp);

        return bestemming;
    }

    private void procesBestuurlijkeGrens(UpdateCounter counter, BestuurlijkGebied bestuurlijkGebied, LocalDateTime timestamp) {
        log.info("Bestuurlijke gebied - domein: {}, identificatie: {}, type: {}", bestuurlijkGebied.getDomein(), bestuurlijkGebied.getIdentificatie(), bestuurlijkGebied.getType());

        // Check if there is already a known bestuurlijkgebied
        List<BestuurlijkGebiedDto> bestuurlijkGebiedDtoList = bestuurlijkeGebiedenStorageService.findByIdentificatie(bestuurlijkGebied.getIdentificatie());
        int size = bestuurlijkGebiedDtoList.size();
        if (size == 0) { // no entries found
            log.debug("Create and store new entry for identificatie: {}", bestuurlijkGebied.getIdentificatie());

            //BestuurlijkGebiedDto bestuurlijkGebiedDto = toDto(bestuurlijkGebied);
            SynchBestuurlijkgebied synchBestuurlijkgebied = toSyncDto(bestuurlijkGebied);

            if (synchBestuurlijkgebied.getBestuurlijkGebiedDto() != null) {
                BestuurlijkGebiedDto bestuurlijkGebiedDto = synchBestuurlijkgebied.getBestuurlijkGebiedDto();
                LocatieDto locatieDto = synchBestuurlijkgebied.getLocatieDto();

                if ((locatieDto == null) || (locatieDto.getGeometrie() == null)) {
                    log.error("Invalud locatieDto");
                }

                locatieDto.setRegistratie(timestamp);
                // Fill required fields
                //bestuurlijkGebiedDto.setMd5hash(DigestUtils.md5Hex(bestuurlijkGebied.getGeometrie().toString().toUpperCase()));
                bestuurlijkGebiedDto.setBeginRegistratie(timestamp);
                // Save entry
                bestuurlijkeGebiedenStorageService.save(bestuurlijkGebiedDto);
                locatieStorageService.saveOrUpdate(locatieDto);
                counter.add();
            } else {
                log.error("Skipped bestuurlijkgebied with identificatie: {} - mapping error", bestuurlijkGebied.getIdentificatie());
                counter.skipped();
            }
        } else {
            if (size == 1) { // exactly 1 entrie found, update
                addNewEntry(bestuurlijkGebied, bestuurlijkGebiedDtoList, counter, timestamp);
            } else { // size > 1 order is beginregistratie desc, begingeldigheid desc so first entry must be used as last entry
                log.info("Found {} hits for identificatie: {}", size, bestuurlijkGebied.getIdentificatie());
                addNewEntry(bestuurlijkGebied, bestuurlijkGebiedDtoList, counter, timestamp);
            }
        }
    }

    private void addNewEntry(BestuurlijkGebied bestuurlijkGebied, List<BestuurlijkGebiedDto> bestuurlijkGebiedDtoList, UpdateCounter counter, LocalDateTime timestamp) {
        if (!compairBestuurlijkgebied(bestuurlijkGebied, bestuurlijkGebiedDtoList.get(0))) {
            log.debug("Update and store entry for identificatie: {}", bestuurlijkGebied.getIdentificatie());

            BestuurlijkGebiedDto currentDto = bestuurlijkGebiedDtoList.get(0);
            BestuurlijkGebiedDto copyDto = copyToDto(currentDto, timestamp);
            //BestuurlijkGebiedDto lastDto = toDto(bestuurlijkGebied);

            SynchBestuurlijkgebied synchBestuurlijkgebied = toSyncDto(bestuurlijkGebied);
            BestuurlijkGebiedDto lastDto = synchBestuurlijkgebied.getBestuurlijkGebiedDto();
            LocatieDto locatieDto = synchBestuurlijkgebied.getLocatieDto();
            locatieDto.setRegistratie(timestamp);

            if ((locatieDto == null) || (locatieDto.getGeometrie() == null)) {
                log.error("Invalud locatieDto");
            }

            // update current eindregistratie
            currentDto.setEindRegistratie(timestamp);
            lastDto.setBeginRegistratie(timestamp);
            lastDto.setMd5hash(DigestUtils.md5Hex(bestuurlijkGebied.getGeometrie().toString().toUpperCase()));
            // save historie
            bestuurlijkeGebiedenStorageService.saveWithHistory(currentDto, copyDto, lastDto);
            locatieStorageService.saveOrUpdate(locatieDto);

            counter.updated();
        } else {
            log.debug("Identical entry for identificatie: {}", bestuurlijkGebied.getIdentificatie());
            counter.unmodified();
        }
    }

    public BestuurlijkeGebiedenGet200Response getBestuurlijkGebiedPage(Integer page, Integer size, LocalDate validAt) {

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/bestuurlijke-gebieden");

        uriComponentsBuilder.queryParam("type", "territoriaalInclusiefEEZ");
        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
        uriComponentsBuilder.queryParam("geldigOp", validAt.toString());
        log.debug("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), BestuurlijkeGebiedenGet200Response.class);
    }
}

