package nl.bsoft.apidemo.synchroniseren.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.synchroniseren.service.BestuurlijkeGrenzenProcessingService;
import nl.bsoft.apidemo.synchroniseren.service.OpenbareLichamenProcessingService;
import nl.bsoft.apidemo.synchroniseren.service.UpdateCounter;
import nl.bsoft.apidemo.synchroniseren.util.TaskSemaphore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@EnableScheduling
@Service
@RequiredArgsConstructor
public class SynchroniseTask {

    private final BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService;
    private final OpenbareLichamenProcessingService openbareLichamenProcessingService;
    private final TaskSemaphore taskSemaphore = TaskSemaphore.getINSTANCE();
    @Value("${nl.bsoft.apidemo.config.scheduleEnabled}")
    private boolean scheduleEnabled;

    /*
    Cron syntax
    <second> <minute> <hour> <day-of-month> <month> <day-of-week>
     */
    @Scheduled(cron = "0 0 22 * * ?", zone = "Europe/Amsterdam")
    public void scheduleTask() {
        LocalDateTime now = LocalDateTime.now();

        log.info("Executing task scheduleTask at: {} scheduledEnabled: {}", now, scheduleEnabled);

        if (scheduleEnabled) {
            UpdateCounter counter = new UpdateCounter();

            boolean freeTask;

            freeTask = taskSemaphore.getTaskSlot();
            if (freeTask) {
                counter = bestuurlijkeGrenzenProcessingService.processBestuurlijkeGebieden();
                taskSemaphore.releaseTask();
                log.info("bestuurlijkegrenzen: {}", counter.toString());
            } else {
                log.info("There is another task running to update bestuurlijkegebieden");
            }

            freeTask = taskSemaphore.getTaskSlot();
            if (freeTask) {
                counter = openbareLichamenProcessingService.processOpenbareLichamen();
                taskSemaphore.releaseTask();
                log.info("openbarelichamen: {}", counter.toString());
            } else {
                log.info("There is another task running to update openbarelichamen");
            }
        }
    }
}
