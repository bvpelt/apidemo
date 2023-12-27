package nl.bsoft.apidemo.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.mapper.OpenbaarLichaamMapper;
import nl.bsoft.apidemo.library.mapper.OpenbaarLichaamMapperImpl;
import nl.bsoft.apidemo.library.model.dto.OpenbaarLichaamDto;
import nl.bsoft.apidemo.library.service.APIService;
import nl.bsoft.apidemo.library.service.OpenbaarLichaamStorageService;
import nl.bsoft.bestuurlijkegrenzen.generated.model.OpenbaarLichaam;
import nl.bsoft.bestuurlijkegrenzen.generated.model.OpenbareLichamenGet200Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class OpenbareLichamenImportService {
    private static final int MAX_PAGE_SIZE = 50;
    private final OpenbaarLichaamStorageService openbaarLichaamStorageService;

    private final OpenbaarLichaamMapper openbaarLichaamMapper = new OpenbaarLichaamMapperImpl();
    private final APIService APIService;

    @Autowired
    public OpenbareLichamenImportService(APIService APIService, OpenbaarLichaamStorageService openbaarLichaamStorageService) {
        this.APIService = APIService;
        this.openbaarLichaamStorageService = openbaarLichaamStorageService;
    }

    public UpdateCounter getAllOpenbareLichamen() {
        int page = 1;
        UpdateCounter counter = new UpdateCounter();
        boolean morePages = true;
        while (morePages) {
            OpenbareLichamenGet200Response openbareLichamenGet200Response = getOpenbaarLichaamPage(page, MAX_PAGE_SIZE);

            if (openbareLichamenGet200Response != null) {
                if (openbareLichamenGet200Response.getEmbedded() != null) {
                    List<OpenbaarLichaam> openbaarLichaamList = openbareLichamenGet200Response.getEmbedded().getOpenbareLichamen();
                    log.debug("Retrieved page: {}", page);
                    if ((openbaarLichaamList != null) && (openbaarLichaamList.size() > 0)) {
                        processOpenbareLichamen(counter, openbaarLichaamList);
                    } else {
                        log.info("No results in the list");
                    }
                    if (openbareLichamenGet200Response.getLinks().getNext() != null) {
                        page++;
                    } else {
                        log.info("No more pages available");
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
        return counter;
    }

    public OpenbareLichamenGet200Response getOpenbaarLichaamPage(Integer page, Integer size) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/openbare-lichamen");

        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
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


        return bestemming;
    }

    private void processOpenbareLichaam(UpdateCounter counter, OpenbaarLichaam openbaarLichaam) {
        log.info("Openbaarlichaam - code: {}, oin: {}, type: {}, naam: {}, bestuurslaag: {}",
                openbaarLichaam.getCode().isPresent() ? openbaarLichaam.getCode().get() : "",
                openbaarLichaam.getOin().isPresent() ? openbaarLichaam.getOin().get() : "",
                openbaarLichaam.getType().getValue(),
                openbaarLichaam.getNaam(),
                openbaarLichaam.getBestuurslaag().getValue());


        // Check if there is already a known bestuurlijkgebied
        List<OpenbaarLichaamDto> openbaarLichaamDtoList = openbaarLichaamStorageService.findByIdentificatie(openbaarLichaam.getCode().get());
        int size = openbaarLichaamDtoList.size();
        if (size == 0) { // no entries found
            log.info("Create and store new entry for code: {}", openbaarLichaam.getCode().get());
            OpenbaarLichaamDto openbaarLichaamDto = toDto(openbaarLichaam);
            LocalDateTime registrationMoment = LocalDateTime.now();
            openbaarLichaamDto.setBeginRegistratie(registrationMoment);

            openbaarLichaamStorageService.Save(openbaarLichaamDto);
            counter.add();
        } else {
            if (size == 1) { // exactly 1 entrie found, update

                if (!compairOpenbaarLichaam(openbaarLichaam, openbaarLichaamDtoList.get(0))) {
                    log.info("Update and store entry for code: {}", openbaarLichaam.getCode().get());

                    LocalDateTime registrationMoment = LocalDateTime.now();

                    OpenbaarLichaamDto currentDto = openbaarLichaamDtoList.get(0);
                    OpenbaarLichaamDto copyDto = CopyToDto(currentDto, registrationMoment);
                    OpenbaarLichaamDto lastDto = toDto(openbaarLichaam);

                    // update current eindregistratie
                    currentDto.setEindRegistratie(registrationMoment);
                    lastDto.setBeginRegistratie(registrationMoment);
                    // save historie
                    openbaarLichaamStorageService.SaveWithHistory(currentDto, copyDto, lastDto);
                    counter.updated();
                } else {
                    log.info("Identical entry for code: {}", openbaarLichaam.getCode().get());
                    counter.unmodified();
                }
            } else {
                log.error("Not yet implemented");
                counter.skipped();
            }
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

