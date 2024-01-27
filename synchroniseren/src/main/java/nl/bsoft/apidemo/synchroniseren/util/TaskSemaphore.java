package nl.bsoft.apidemo.synchroniseren.util;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskSemaphore {
    private static TaskSemaphore INSTANCE;
    private static AtomicBoolean free;
    private final int MAXTHREADS = 1;
    private final Semaphore semaphore;

    private TaskSemaphore() {
        semaphore = new Semaphore(MAXTHREADS);
        free = new AtomicBoolean(true);
    }

    public static TaskSemaphore getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new TaskSemaphore();

        }
        return INSTANCE;
    }

    public boolean getTaskSlot() {
        return semaphore.tryAcquire();
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
