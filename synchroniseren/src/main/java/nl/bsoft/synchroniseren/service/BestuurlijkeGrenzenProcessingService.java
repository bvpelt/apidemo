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

        return bestuurlijkeGrenzenImportService.getAllBestuurlijkebebieden();

    }





}
