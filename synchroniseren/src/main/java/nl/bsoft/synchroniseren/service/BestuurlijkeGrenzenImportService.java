package nl.bsoft.synchroniseren.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkGebied;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkeGebiedenGet200Response;
import nl.bsoft.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.library.service.APIService;
import nl.bsoft.library.service.BestuurlijkeGebiedenStorageService;
import nl.bsoft.library.service.GeoService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
public class BestuurlijkeGrenzenImportService {

    private static final int MAX_PAGE_SIZE = 50;
    private final APIService APIService;
    private final GeoService geoService;
    private final BestuurlijkeGebiedenStorageService bestuurlijkeGebiedenStorageService;


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


    private void procesBestuurlijkeGrens(UpdateCounter counter, BestuurlijkGebied bestuurlijkGebied) {
        log.info("Bestuurlijke gebied - domein: {}, identificatie: {}, type: {}", bestuurlijkGebied.getDomein(), bestuurlijkGebied.getIdentificatie(), bestuurlijkGebied.getType());


        // Check if there is already a known bestuurlijkgebied
        List<BestuurlijkGebiedDto> bestuurlijkGebiedDtoList = bestuurlijkeGebiedenStorageService.findByIdentificatie(bestuurlijkGebied.getIdentificatie());
        int size = bestuurlijkGebiedDtoList.size();
        if (size == 0) { // no entries found
            log.info("Create and store new entry for identificatie: {}", bestuurlijkGebied.getIdentificatie());
            BestuurlijkGebiedDto bestuurlijkGebiedDto = new BestuurlijkGebiedDto();
            bestuurlijkGebiedDto.setIdentificatie(bestuurlijkGebied.getIdentificatie());
            bestuurlijkGebiedDto.setType(bestuurlijkGebied.getType().getValue());
            bestuurlijkGebiedDto.setDomein(bestuurlijkGebied.getDomein());
            bestuurlijkGebiedDto.setMd5hash(DigestUtils.md5Hex(bestuurlijkGebied.getGeometrie().toString().toUpperCase()));
            bestuurlijkGebiedDto.setBeginGeldigheid(bestuurlijkGebied.getEmbedded().getMetadata().getBeginGeldigheid().get());
            bestuurlijkGebiedDto.setRegistratieTijdstip(LocalDate.now());
            if (bestuurlijkGebied.getEmbedded().getMetadata().getEindGeldigheid().isPresent()) {
                bestuurlijkGebiedDto.setEindGeldigheid(bestuurlijkGebied.getEmbedded().getMetadata().getEindGeldigheid().get());
            }
            bestuurlijkGebiedDto.setGeometrie(geoService.geoJsonToJTS(bestuurlijkGebied.getGeometrie()));
            bestuurlijkeGebiedenStorageService.Save(bestuurlijkGebiedDto);
            counter.add();
        } else {
            if (size == 1) { // exactly 1 entrie found, update

                if (!compairBestuurlijkgebied(bestuurlijkGebied, bestuurlijkGebiedDtoList.get(0))) {
                    log.info("Update and store entry for identificatie: {}", bestuurlijkGebied.getIdentificatie());
                    BestuurlijkGebiedDto bestuurlijkGebiedDto = bestuurlijkGebiedDtoList.get(0);
                    bestuurlijkGebiedDto.setType(bestuurlijkGebied.getType().getValue());
                    bestuurlijkGebiedDto.setDomein(bestuurlijkGebied.getDomein());
                    bestuurlijkGebiedDto.setBeginGeldigheid(bestuurlijkGebied.getEmbedded().getMetadata().getBeginGeldigheid().get());
                    bestuurlijkGebiedDto.setMd5hash(DigestUtils.md5Hex(bestuurlijkGebied.getGeometrie().toString().toUpperCase()));
                    if (bestuurlijkGebied.getEmbedded().getMetadata().getEindGeldigheid().isPresent()) {
                        bestuurlijkGebiedDto.setEindGeldigheid(bestuurlijkGebied.getEmbedded().getMetadata().getEindGeldigheid().get());
                    }
                    bestuurlijkGebiedDto.setGeometrie(geoService.geoJsonToJTS(bestuurlijkGebied.getGeometrie()));
                    bestuurlijkeGebiedenStorageService.Save(bestuurlijkGebiedDto);
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

        uriComponentsBuilder.queryParam("type", "territoriaal");
        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
        log.debug("using url: {}", uriComponentsBuilder.build().toUri());
        return APIService.getDirectly(uriComponentsBuilder.build().toUri(), BestuurlijkeGebiedenGet200Response.class);
    }
}

