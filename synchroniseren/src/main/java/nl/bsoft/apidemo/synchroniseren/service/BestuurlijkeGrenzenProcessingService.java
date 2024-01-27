package nl.bsoft.apidemo.synchroniseren.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class BestuurlijkeGrenzenProcessingService {

    private BestuurlijkeGrenzenImportService bestuurlijkeGrenzenImportService;

    @Autowired
    public BestuurlijkeGrenzenProcessingService(BestuurlijkeGrenzenImportService bestuurlijkeGrenzenImportService) {
        this.bestuurlijkeGrenzenImportService = bestuurlijkeGrenzenImportService;
    }

    @Async
    public UpdateCounter processBestuurlijkeGebieden(LocalDate validAt) {
        log.debug("Process all bestuurlijke gebieden");

        return bestuurlijkeGrenzenImportService.getAllBestuurlijkebebieden(validAt);
    }

}
