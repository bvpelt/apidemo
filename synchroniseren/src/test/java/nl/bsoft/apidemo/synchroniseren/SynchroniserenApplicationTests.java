package nl.bsoft.apidemo.synchroniseren;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.synchroniseren.service.JobStatus;
import nl.bsoft.apidemo.synchroniseren.util.TaskSemaphore;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.util.Assert;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
class SynchroniserenApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testSemaphore_whenReachLimit_thenBlocked() {
        int threads = 2;

        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        TaskSemaphore taskSemaphore = TaskSemaphore.getINSTANCE();

        try {
            IntStream.range(0, threads)
                    .forEach(user -> executorService.execute(taskSemaphore::getTaskSlot));
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error("Error duing execution of task");
        }
        executorService.shutdown();
        log.debug("Executor stopped");

        Assert.equals(0, taskSemaphore.availableSlots());
        Assert.equals(false, taskSemaphore.getTaskSlot());

    }

    @Test
    public void testSemaphore_whenRelease_thenSlotsAvailable() {
        int threads = 2;

        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        TaskSemaphore taskSemaphore = TaskSemaphore.getINSTANCE();

        try {
            IntStream.range(0, threads)
                    .forEach(user -> executorService.execute(taskSemaphore::getTaskSlot));
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error("Error duing execution of task");
        }
        executorService.shutdown();

        Assert.equals(0, taskSemaphore.availableSlots());
        taskSemaphore.releaseTask();
        Assert.equals(true, taskSemaphore.availableSlots() > 0);
        Assert.equals(1, taskSemaphore.availableSlots());
        Assert.equals(true, taskSemaphore.getTaskSlot());

    }

    @Test
    public void testJobStatus() {
        String status;
        log.info("Status start: {}", JobStatus.START);
        Assert.equals("START", JobStatus.START.name());

        status = JobStatus.START.name();
        log.info("Status start: {}", status);

        status = JobStatus.START.toString();
        Assert.equals("START", status);
        log.info("Status start: {}", status);
    }
}
