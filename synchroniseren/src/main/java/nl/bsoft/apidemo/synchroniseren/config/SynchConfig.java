package nl.bsoft.apidemo.synchroniseren.config;

import nl.bsoft.apidemo.synchroniseren.util.TaskSemaphore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SynchConfig {
    private final TaskSemaphore taskSemaphore = TaskSemaphore.getINSTANCE();
    @Bean
    public TaskSemaphore getTaskSemaphore() {
        return this.taskSemaphore;
    }
}
