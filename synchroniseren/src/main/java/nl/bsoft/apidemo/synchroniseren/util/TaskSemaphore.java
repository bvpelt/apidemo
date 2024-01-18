package nl.bsoft.apidemo.synchroniseren.util;

import java.util.concurrent.Semaphore;

public class TaskSemaphore {

    private static TaskSemaphore INSTANCE;
    private final int MAXTHREADS = 1;
    private final Semaphore semaphore;

    private TaskSemaphore() {
        semaphore = new Semaphore(MAXTHREADS);
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

}
