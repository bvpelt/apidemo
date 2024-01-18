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

    private final TaskSemaphore taskSemaphore = TaskSemaphore.getINSTANCE();
    private BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService;
    private OpenbareLichamenProcessingService openbareLichamenProcessingService;

    @Autowired
    SynchronizeController(BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService, OpenbareLichamenProcessingService openbareLichamenProcessingService) {
        this.bestuurlijkeGrenzenProcessingService = bestuurlijkeGrenzenProcessingService;
        this.openbareLichamenProcessingService = openbareLichamenProcessingService;
    }

    @GetMapping("/bestuurlijkegebieden")
    public ResponseEntity startBestuurlijkgebied() {
        boolean freeTask = taskSemaphore.getTaskSlot();
        UpdateCounter counter = new UpdateCounter();

        if (freeTask) {
            counter = bestuurlijkeGrenzenProcessingService.processBestuurlijkeGebieden();

            taskSemaphore.releaseTask();

            log.info("End   processing - {} elements", counter.getProcessed());
        } else {
            log.info("There is another task running to update bestuurlijkegebieden");
        }

        return ResponseEntity.ok(counter);
    }

    @GetMapping("/openbarelichamen")
    public ResponseEntity startOpenbaarLichaam() {
        boolean freeTask = taskSemaphore.getTaskSlot();
        UpdateCounter counter = new UpdateCounter();

        if (freeTask) {
            counter = openbareLichamenProcessingService.processOpenbareLichamen();

            taskSemaphore.releaseTask();

            log.info("End   processing - {} elements", counter.getProcessed());
        } else {
            log.info("There is another task running to update openbarelichamen");
        }

        return ResponseEntity.ok(counter);
    }
}
