package nl.bsoft.apidemo.synchroniseren.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class TaskSemaphore {
    private static TaskSemaphore INSTANCE;
    private static AtomicBoolean free;
    private final int MAXTHREADS = 1;
    private final Semaphore semaphore;

    public TaskSemaphore() {
        semaphore = new Semaphore(MAXTHREADS);
        free = new AtomicBoolean(true);
        log.debug("Created - available: {}", semaphore.availablePermits());
    }

    public static TaskSemaphore getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new TaskSemaphore();
        }
        return INSTANCE;
    }

    public boolean getTaskSlot() {
        log.debug("Available: {}", semaphore.availablePermits());
        boolean result = semaphore.tryAcquire();
        log.debug("Got semaphore: {}", result);
        return result;
    }

    public void releaseTask() {
        semaphore.release();
    }

    public int availableSlots() {
        return semaphore.availablePermits();
    }

    public boolean getAndSetFree(boolean value) {
        return free.getAndSet(value);
    }
}
