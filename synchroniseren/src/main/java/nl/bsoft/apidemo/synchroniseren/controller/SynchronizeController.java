package nl.bsoft.apidemo.synchroniseren.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.synchroniseren.service.BestuurlijkeGrenzenProcessingService;
import nl.bsoft.apidemo.synchroniseren.service.OpenbareLichamenProcessingService;
import nl.bsoft.apidemo.synchroniseren.service.UpdateCounter;
import nl.bsoft.apidemo.synchroniseren.util.TaskSemaphore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SynchronizeController {
    private BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService;
    private OpenbareLichamenProcessingService openbareLichamenProcessingService;

    @Autowired
    SynchronizeController(BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService, OpenbareLichamenProcessingService openbareLichamenProcessingService, TaskSemaphore taskSemaphore) {
        this.bestuurlijkeGrenzenProcessingService = bestuurlijkeGrenzenProcessingService;
        this.openbareLichamenProcessingService = openbareLichamenProcessingService;
    }

    @GetMapping("/bestuurlijkegebieden")
    public ResponseEntity startBestuurlijkgebied() {

        UpdateCounter counter = new UpdateCounter();

        counter = bestuurlijkeGrenzenProcessingService.processBestuurlijkeGebieden();

        return ResponseEntity.ok(counter);
    }

    @GetMapping("/openbarelichamen")
    public ResponseEntity startOpenbaarLichaam() {

        UpdateCounter counter = new UpdateCounter();

        counter = openbareLichamenProcessingService.processOpenbareLichamen();

        return ResponseEntity.ok(counter);
    }
}
