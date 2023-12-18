package nl.bsoft.synchroniseren.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CompletableFuture;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkGebied;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkeGebiedenGet200Response;
import nl.bsoft.library.service.BestuurlijkGebiedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.function.Consumer;


@Slf4j
@Service
public class BestuurlijkeGrenzenImportService {

    private static final int MAX_PAGE_SIZE = 50;
    private final BestuurlijkGebiedService bestuurlijkGebiedService;

    @Autowired
    public BestuurlijkeGrenzenImportService(BestuurlijkGebiedService bestuurlijkGebiedService) {
        this.bestuurlijkGebiedService = bestuurlijkGebiedService;
    }
    public void processAllBestuurlijkgebieden(Consumer<List<BestuurlijkGebied>> callable) {
        log.info("Start import bestuurlijkegebieden");

        bestuurlijkGebiedService.processAllItemsFromCompletableFuture(
                (pageIndex, size) -> getBestuurlijkegebieden(pageIndex, size)
                        .thenApply(
                                bestuurlijkeGrenzenGet200Response -> {
                                    if (bestuurlijkeGrenzenGet200Response != null) {
                                        callable.accept(bestuurlijkeGrenzenGet200Response.getEmbedded().getBestuurlijkeGebieden());
                                    }
                                    return bestuurlijkeGrenzenGet200Response;
                                }),
                bestuurlijkgebied -> {
                    if (bestuurlijkgebied != null && bestuurlijkgebied.getLinks().getNext() == null) {
                        return 0;
                    }
                    return 1;
                });
    }
    public CompletableFuture<BestuurlijkeGebiedenGet200Response> getBestuurlijkegebieden(Integer page, Integer size) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(bestuurlijkGebiedService.getApiUrl() + "/bestuurlijke-gebieden");

        //uriComponentsBuilder.queryParam("sort", "tijdslijnen.beschikbaarVanaf");
        uriComponentsBuilder.queryParam("type", "territoriaal");
        uriComponentsBuilder.queryParam("page", page);
        uriComponentsBuilder.queryParam("pageSize", size);
        log.debug("using url: {}", uriComponentsBuilder.build().toUri().toString());
//        uriComponentsBuilder.queryParam("gemuteerdSinds", gemuteerdSinds);
        return bestuurlijkGebiedService.getIgnoreFailure(uriComponentsBuilder.build().toUri(), BestuurlijkeGebiedenGet200Response.class);
    }
}
