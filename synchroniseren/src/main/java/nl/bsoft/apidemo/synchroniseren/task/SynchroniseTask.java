package nl.bsoft.apidemo.synchroniseren.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.synchroniseren.service.BestuurlijkeGrenzenProcessingService;
import nl.bsoft.apidemo.synchroniseren.service.OpenbareLichamenProcessingService;
import nl.bsoft.apidemo.synchroniseren.service.UpdateCounter;
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

    @Value("${nl.bsoft.apidemo.config.scheduleEnabled}")
    private boolean scheduleEnabled;

    private final BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService;

    private final OpenbareLichamenProcessingService openbareLichamenProcessingService;


    /*
    Cron syntax
    <second> <minute> <hour> <day-of-month> <month> <day-of-week>
     */
    @Scheduled(cron = "0 0 22 * * ?", zone = "Europe/Amsterdam")
    public void scheduleTask() {
        LocalDateTime now = LocalDateTime.now();

        log.info("Executing task scheduleTask at: {} scheduledEnabled: {}", now, scheduleEnabled);

        if (scheduleEnabled) {
            UpdateCounter counter = bestuurlijkeGrenzenProcessingService.processBestuurlijkeGebieden();
            log.info("bestuurlijkegrenzen: {}", counter.toString());

            counter = openbareLichamenProcessingService.processOpenbareLichamen();
            log.info("openbarelichamen: {}", counter.toString());
        }
    }
}
