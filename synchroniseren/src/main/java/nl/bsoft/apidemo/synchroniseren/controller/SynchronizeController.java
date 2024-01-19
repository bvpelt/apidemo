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

    private TaskSemaphore taskSemaphore;
    private BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService;
    private OpenbareLichamenProcessingService openbareLichamenProcessingService;

    @Autowired
    SynchronizeController(BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService, OpenbareLichamenProcessingService openbareLichamenProcessingService, TaskSemaphore taskSemaphore) {
        this.bestuurlijkeGrenzenProcessingService = bestuurlijkeGrenzenProcessingService;
        this.openbareLichamenProcessingService = openbareLichamenProcessingService;
        this.taskSemaphore = taskSemaphore;
    }

    @GetMapping("/bestuurlijkegebieden")
    public ResponseEntity startBestuurlijkgebied() {
        boolean freeTask = taskSemaphore.getTaskSlot();
        UpdateCounter counter = new UpdateCounter();

        log.info("Start synchronizing bestuurlijkegebieden, get task");

        if (freeTask) {
            counter = bestuurlijkeGrenzenProcessingService.processBestuurlijkeGebieden();

            taskSemaphore.releaseTask();

            log.info("End   processing - {} elements", counter.getProcessed());
        } else {
            log.info("There is another task running to update bestuurlijkegebieden");
        }

        log.info("End   synchronizing bestuurlijkegebieden, release task");

        return ResponseEntity.ok(counter);
    }

    @GetMapping("/openbarelichamen")
    public ResponseEntity startOpenbaarLichaam() {
        boolean freeTask = taskSemaphore.getTaskSlot();
        UpdateCounter counter = new UpdateCounter();

        log.info("Start synchronizing openbarelichamen, get task");

        if (freeTask) {
            counter = openbareLichamenProcessingService.processOpenbareLichamen();

            taskSemaphore.releaseTask();

            log.info("End   processing - {} elements", counter.getProcessed());
        } else {
            log.info("There is another task running to update openbarelichamen");
        }

        log.info("End   synchronizing openbarelichamen, release task");

        return ResponseEntity.ok(counter);
    }
}
