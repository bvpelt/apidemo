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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@EnableScheduling
@Service
@RequiredArgsConstructor
public class SynchroniseTask {
    private final BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService;
    private final OpenbareLichamenProcessingService openbareLichamenProcessingService;
    @Value("${nl.bsoft.apidemo.config.scheduleEnabled}")
    private boolean scheduleEnabled;

    /*
    Cron syntax
    <second> <minute> <hour> <day-of-month> <month> <day-of-week>
    pattern for each day at 22:00 is "0 0 22 * * ?"
    */
    @Scheduled(cron = "0 5/10 * * * *", zone = "Europe/Amsterdam")
    public void scheduleTask() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate currentDate = LocalDate.now();

        log.info("Executing task scheduleTask at: {} scheduledEnabled: {}", now, scheduleEnabled);

        if (scheduleEnabled) {
            UpdateCounter counter = new UpdateCounter();

            counter = bestuurlijkeGrenzenProcessingService.processBestuurlijkeGebieden(currentDate);
            log.info("Bestuurlijkegrenzen scheduled task result: {}", counter.toString());

            counter = openbareLichamenProcessingService.processOpenbareLichamen(currentDate);
            log.info("Openbarelichamen scheduled task result: {}", counter.toString());
        } else {
            log.info("Scheduling not enabled");
        }
    }
}
