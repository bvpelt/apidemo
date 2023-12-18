package nl.bsoft.synchroniseren.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkGebied;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class BestuurlijkeGrenzenProcessingService {

    private BestuurlijkeGrenzenImportService bestuurlijkeGrenzenImportService;

    @Autowired
    public BestuurlijkeGrenzenProcessingService(BestuurlijkeGrenzenImportService bestuurlijkeGrenzenImportService) {
        this.bestuurlijkeGrenzenImportService = bestuurlijkeGrenzenImportService;
    }

    public int processBestuurlijkeGebieden() {
        log.info("Process all bestuurlijke gebieden");

        AtomicInteger counter = new AtomicInteger();
        bestuurlijkeGrenzenImportService.processAllBestuurlijkgebieden(
                bestuurlijkGebieds -> {
            counter.addAndGet(processBestuurlijkeGrenzen(bestuurlijkGebieds));
        });

        return counter.get();
    }


    private int processBestuurlijkeGrenzen(List<BestuurlijkGebied> bestuurlijkeGebieden) {

        return bestuurlijkeGebieden.size();
    }

    /*
    private int processDocuments(List<Document> documentList) {
        Map<String, Set<DocumentLocatie>> locationCollection =
                locatieSynchronisatieService.getLocations(documentList);
        List<Document> documents = getDocumentsWithGeo(documentList, locationCollection);
        log.info("[OZON] Save {} regelingen.", documents.size());
        documentService.saveAllDocuments(documents);
        return documentList.size();
    }
    */

}
