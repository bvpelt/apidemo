package nl.bsoft.synchroniseren.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkGebied;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkeGebiedenGet200Response;
import nl.bsoft.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.library.service.BestuurlijkGebiedService;
import nl.bsoft.library.service.BestuurlijkeGebiedenStorageService;
import nl.bsoft.library.service.GeoService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


@Slf4j
@Service
public class BestuurlijkeGrenzenImportService {

    private static final int MAX_PAGE_SIZE = 50;
    private final BestuurlijkGebiedService bestuurlijkGebiedService;
    private final GeoService geoService;
    private final BestuurlijkeGebiedenStorageService bestuurlijkeGebiedenStorageService;

    @Autowired
    public BestuurlijkeGrenzenImportService(BestuurlijkGebiedService bestuurlijkGebiedService, BestuurlijkeGebiedenStorageService bestuurlijkeGebiedenStorageService, GeoService geoService) {
        this.bestuurlijkGebiedService = bestuurlijkGebiedService;
        this.bestuurlijkeGebiedenStorageService = bestuurlijkeGebiedenStorageService;
        this.geoService = geoService;
    }

    public void processAllBestuurlijkgebieden(Consumer<List<BestuurlijkGebied>> callable) {
        log.info("Start import bestuurlijkegebieden");


        bestuurlijkGebiedService.processAllItemsFromCompletableFuture(
                (pageIndex, size) -> getBestuurlijkegebieden(pageIndex + 1, size)
                        .thenApply(
                                bestuurlijkeGrenzenGet200Response -> {
                                    if (bestuurlijkeGrenzenGet200Response != null) {
                                        log.info("Response exists, size: {} ", bestuurlijkeGrenzenGet200Response.getEmbedded().getBestuurlijkeGebieden().size());
                                        callable.accept(bestuurlijkeGrenzenGet200Response.getEmbedded().getBestuurlijkeGebieden());
                                    }
                                    return bestuurlijkeGrenzenGet200Response;
                                }),
                bestuurlijkgebied -> {
                    int index = 1;
                    if (bestuurlijkgebied != null && bestuurlijkgebied.getLinks().getNext() == null) {
                        return 0;
                    }
                    return 1;
                });
    }


    public int getAllBestuurlijkebebieden() {
        int page = 1;
        boolean morePages = true;
        while (morePages) {
            BestuurlijkeGebiedenGet200Response bestuurlijkeGebiedenGet200Response = getPage(page, MAX_PAGE_SIZE);
            if (bestuurlijkeGebiedenGet200Response != null) {
                if (bestuurlijkeGebiedenGet200Response.getEmbedded() != null) {
                    List<BestuurlijkGebied> bestuurlijkGebiedList = bestuurlijkeGebiedenGet200Response.getEmbedded().getBestuurlijkeGebieden();
                    log.debug("Retrieved page: {}", page);
                    if ((bestuurlijkGebiedList != null) && (bestuurlijkGebiedList.size() > 0)) {
                        processBestuurlijkeGrenzen(bestuurlijkGebiedList);
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
        return page;
    }

    private int processBestuurlijkeGrenzen(List<BestuurlijkGebied> bestuurlijkeGebieden) {

        bestuurlijkeGebieden.stream().forEach(
                (bestuurlijkGebied -> {
                    procesBestuurlijkeGrens(bestuurlijkGebied);
                })
        );

        return bestuurlijkeGebieden.size();
    }

    private void procesBestuurlijkeGrens(BestuurlijkGebied bestuurlijkGebied) {
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
            bestuurlijkGebiedDto.setGeometrie(geoService.geoJsonToJTS(bestuurlijkGebied.getGeometrie()));
            bestuurlijkeGebiedenStorageService.Save(bestuurlijkGebiedDto);

        } else {
            if (size == 1) { // exactly 1 entrie found, update
                log.info("Update and store entry for identificatie: {}", bestuurlijkGebied.getIdentificatie());
                BestuurlijkGebiedDto bestuurlijkGebiedDto = bestuurlijkGebiedDtoList.get(0);
                bestuurlijkGebiedDto.setType(bestuurlijkGebied.getType().getValue());
                bestuurlijkGebiedDto.setDomein(bestuurlijkGebied.getDomein());
                bestuurlijkGebiedDto.setGeometrie(geoService.geoJsonToJTS(bestuurlijkGebied.getGeometrie()));
                bestuurlijkGebiedDto.setMd5hash(DigestUtils.md5Hex(bestuurlijkGebied.getGeometrie().toString().toUpperCase()));
                bestuurlijkeGebiedenStorageService.Save(bestuurlijkGebiedDto);
            } else {
                log.error("Not yet implemented");
            }
        }
    }

    public BestuurlijkeGebiedenGet200Response getPage(Integer page, Integer size) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(bestuurlijkGebiedService.getApiUrl() + "/bestuurlijke-gebieden");

        //uriComponentsBuilder.queryParam("sort", "tijdslijnen.beschikbaarVanaf");
        uriComponentsBuilder.queryParam("type", "territoriaal");
        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
        log.debug("using url: {}", uriComponentsBuilder.build().toUri());
        return bestuurlijkGebiedService.getDirectly(uriComponentsBuilder.build().toUri(), BestuurlijkeGebiedenGet200Response.class);
    }

    public CompletableFuture<BestuurlijkeGebiedenGet200Response> getBestuurlijkegebieden(Integer page, Integer size) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(bestuurlijkGebiedService.getApiUrl() + "/bestuurlijke-gebieden");

        //uriComponentsBuilder.queryParam("sort", "tijdslijnen.beschikbaarVanaf");
        uriComponentsBuilder.queryParam("type", "territoriaal");
        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
        log.debug("using url: {}", uriComponentsBuilder.build().toUri());
//        uriComponentsBuilder.queryParam("gemuteerdSinds", gemuteerdSinds);
        return bestuurlijkGebiedService.getIgnoreFailure(uriComponentsBuilder.build().toUri(), BestuurlijkeGebiedenGet200Response.class);
    }
}

