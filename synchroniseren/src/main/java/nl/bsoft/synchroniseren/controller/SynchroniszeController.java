package nl.bsoft.synchroniseren.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.synchroniseren.service.BestuurlijkeGrenzenProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SynchroniszeController {

    private BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService;

    @Autowired
    SynchroniszeController(BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService) {
        this.bestuurlijkeGrenzenProcessingService = bestuurlijkeGrenzenProcessingService;
    }

    @GetMapping("/bestuurlijkegebied")
    public ResponseEntity start() {
        String message = "Ok";

        log.info("Start processing");

        int number = bestuurlijkeGrenzenProcessingService.processBestuurlijkeGebieden();

        log.info("End   processing - {} elements", number);

        return ResponseEntity.ok(message);
    }
}
