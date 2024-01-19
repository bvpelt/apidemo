package nl.bsoft.apidemo.synchroniseren;

import nl.bsoft.apidemo.synchroniseren.util.TaskSemaphore;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.util.Assert;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@SpringBootTest
class SynchroniserenApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testSemaphore_whenReachLimit_thenBlocked() {
        int threads = 2;

        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        TaskSemaphore taskSemaphore = TaskSemaphore.getINSTANCE();

        IntStream.range(0, threads)
                .forEach(user -> executorService.execute(taskSemaphore::getTaskSlot));
        executorService.shutdown();

        Assert.equals(0, taskSemaphore.availableSlots());
        Assert.equals(false, taskSemaphore.getTaskSlot());

    }

    @Test
    public void testSemaphore_whenRelease_thenSlotsAvailable() {
        int threads = 2;

        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        TaskSemaphore taskSemaphore = TaskSemaphore.getINSTANCE();

        IntStream.range(0, threads)
                .forEach(user -> executorService.execute(taskSemaphore::getTaskSlot));
        executorService.shutdown();

        Assert.equals(0, taskSemaphore.availableSlots());
        taskSemaphore.releaseTask();
        Assert.equals(true, taskSemaphore.availableSlots() > 0);
        Assert.equals(1, taskSemaphore.availableSlots());
        Assert.equals(true, taskSemaphore.getTaskSlot());

    }
}
