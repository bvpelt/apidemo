package nl.bsoft.apidemo.synchroniseren.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.mapper.BestuurlijkgebiedMapper;
import nl.bsoft.apidemo.library.mapper.BestuurlijkgebiedMapperImpl;
import nl.bsoft.apidemo.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.apidemo.library.service.APIService;
import nl.bsoft.apidemo.library.service.BestuurlijkeGebiedenStorageService;
import nl.bsoft.apidemo.library.service.GeoService;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkGebied;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkeGebiedenGet200Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BestuurlijkeGrenzenImportService {
    private static final int MAX_PAGE_SIZE = 50;
    private final APIService APIService;
    private final GeoService geoService;
    private final BestuurlijkeGebiedenStorageService bestuurlijkeGebiedenStorageService;
    private final BestuurlijkgebiedMapper bestuurlijkgebiedMapper = new BestuurlijkgebiedMapperImpl();

    @Autowired
    public BestuurlijkeGrenzenImportService(APIService APIService, BestuurlijkeGebiedenStorageService bestuurlijkeGebiedenStorageService, GeoService geoService) {
        this.APIService = APIService;
        this.bestuurlijkeGebiedenStorageService = bestuurlijkeGebiedenStorageService;
        this.geoService = geoService;
    }

    public UpdateCounter getAllBestuurlijkebebieden() {
        int page = 1;
        UpdateCounter counter = new UpdateCounter();
        boolean morePages = true;
        while (morePages) {
            BestuurlijkeGebiedenGet200Response bestuurlijkeGebiedenGet200Response = getBestuurlijkGebiedPage(page, MAX_PAGE_SIZE);
            if (bestuurlijkeGebiedenGet200Response != null) {
                if (bestuurlijkeGebiedenGet200Response.getEmbedded() != null) {
                    List<BestuurlijkGebied> bestuurlijkGebiedList = bestuurlijkeGebiedenGet200Response.getEmbedded().getBestuurlijkeGebieden();
                    log.debug("Retrieved page: {}", page);
                    if ((bestuurlijkGebiedList != null) && (bestuurlijkGebiedList.size() > 0)) {
                        processBestuurlijkeGrenzen(counter, bestuurlijkGebiedList);
                    } else {
                        log.info("No results in the list");
                    }
                    if (bestuurlijkeGebiedenGet200Response.getLinks().getNext() != null) {
                        page++;
                    } else {
                        log.info("No more pages available");
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
        return counter;
    }

    private int processBestuurlijkeGrenzen(UpdateCounter counter, List<BestuurlijkGebied> bestuurlijkeGebieden) {

        bestuurlijkeGebieden.stream().forEach(
                (bestuurlijkGebied -> {
                    procesBestuurlijkeGrens(counter, bestuurlijkGebied);
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

    private BestuurlijkGebiedDto CopyToDto(BestuurlijkGebiedDto bron, LocalDateTime registratieMoment) {
        BestuurlijkGebiedDto bestemming = new BestuurlijkGebiedDto();
        bestemming.setIdentificatie(bron.getIdentificatie());
        bestemming.setDomein(bron.getDomein());
        bestemming.setType(bron.getType());
        bestemming.setMd5hash(bron.getMd5hash());
        bestemming.setBeginGeldigheid(bron.getBeginGeldigheid());
        bestemming.setEindGeldigheid(bron.getEindGeldigheid());
        bestemming.setGeometrie(bron.getGeometrie());
        bestemming.setBeginRegistratie(registratieMoment);

        return bestemming;
    }

    private void procesBestuurlijkeGrens(UpdateCounter counter, BestuurlijkGebied bestuurlijkGebied) {
        log.info("Bestuurlijke gebied - domein: {}, identificatie: {}, type: {}", bestuurlijkGebied.getDomein(), bestuurlijkGebied.getIdentificatie(), bestuurlijkGebied.getType());

        // Check if there is already a known bestuurlijkgebied
        List<BestuurlijkGebiedDto> bestuurlijkGebiedDtoList = bestuurlijkeGebiedenStorageService.findByIdentificatie(bestuurlijkGebied.getIdentificatie());
        int size = bestuurlijkGebiedDtoList.size();
        if (size == 0) { // no entries found
            log.info("Create and store new entry for identificatie: {}", bestuurlijkGebied.getIdentificatie());

            BestuurlijkGebiedDto bestuurlijkGebiedDto = toDto(bestuurlijkGebied);

            if (bestuurlijkGebiedDto != null) {
                // Fill required fields
                bestuurlijkGebiedDto.setMd5hash(DigestUtils.md5Hex(bestuurlijkGebied.getGeometrie().toString().toUpperCase()));
                bestuurlijkGebiedDto.setBeginRegistratie(LocalDateTime.now());
                // Save entry
                bestuurlijkeGebiedenStorageService.Save(bestuurlijkGebiedDto);
                counter.add();
            } else {
                log.error("Skipped bestuurlijkgebied with identificatie: {} - mapping error", bestuurlijkGebied.getIdentificatie());
                counter.skipped();
            }
        } else {
            if (size == 1) { // exactly 1 entrie found, update
                if (!compairBestuurlijkgebied(bestuurlijkGebied, bestuurlijkGebiedDtoList.get(0))) {
                    log.info("Update and store entry for identificatie: {}", bestuurlijkGebied.getIdentificatie());

                    LocalDateTime registrationMoment = LocalDateTime.now();

                    BestuurlijkGebiedDto currentDto = bestuurlijkGebiedDtoList.get(0);
                    BestuurlijkGebiedDto copyDto = CopyToDto(currentDto, registrationMoment);
                    BestuurlijkGebiedDto lastDto = toDto(bestuurlijkGebied);

                    // update current eindregistratie
                    currentDto.setEindRegistratie(registrationMoment);
                    lastDto.setBeginRegistratie(registrationMoment);
                    lastDto.setMd5hash(DigestUtils.md5Hex(bestuurlijkGebied.getGeometrie().toString().toUpperCase()));
                    // save historie
                    bestuurlijkeGebiedenStorageService.SaveWithHistory(currentDto, copyDto, lastDto);

                    counter.updated();
                } else {
                    log.info("Identical entry for identificatie: {}", bestuurlijkGebied.getIdentificatie());
                    counter.unmodified();
                }
            } else {
                log.error("Not yet implemented");
                counter.skipped();
            }
        }
    }

    public BestuurlijkeGebiedenGet200Response getBestuurlijkGebiedPage(Integer page, Integer size) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(APIService.getApiUrl() + "/bestuurlijke-gebieden");

        uriComponentsBuilder.queryParam("type", "territoriaalInclusiefEEZ");
        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
        log.debug("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), BestuurlijkeGebiedenGet200Response.class);
    }
}

