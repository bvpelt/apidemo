package nl.bsoft.apidemo.synchroniseren.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.service.BestuurlijkeGrenzenProcessingService;
import nl.bsoft.apidemo.service.OpenbareLichamenProcessingService;
import nl.bsoft.apidemo.service.UpdateCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SynchroniszeController {

    private BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService;

    private OpenbareLichamenProcessingService openbareLichamenProcessingService;

    @Autowired
    SynchroniszeController(BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService, OpenbareLichamenProcessingService openbareLichamenProcessingService) {
        this.bestuurlijkeGrenzenProcessingService = bestuurlijkeGrenzenProcessingService;
        this.openbareLichamenProcessingService = openbareLichamenProcessingService;
    }

    @GetMapping("/bestuurlijkegebieden")
    public ResponseEntity startBestuurlijkgebied() {

        UpdateCounter counter = bestuurlijkeGrenzenProcessingService.processBestuurlijkeGebieden();

        log.info("End   processing - {} elements", counter.getProcessed());

        return ResponseEntity.ok(counter);
    }

    @GetMapping("/openbarelichamen")
    public ResponseEntity startOpenbaarLichaam() {

        UpdateCounter counter = openbareLichamenProcessingService.processOpenbareLichamen();

        log.info("End   processing - {} elements", counter.getProcessed());

        return ResponseEntity.ok(counter);
    }
}
