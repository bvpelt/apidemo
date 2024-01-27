package nl.bsoft.apidemo.presenteren.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@Slf4j
public class SpringBuiltInContextEventsListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Event: =========>>>> {}", event.toString());
        if (event instanceof ContextRefreshedEvent) {
            log.info("Event type: ContextRefreshedEvent\nA ContextRefreshedEvent is fired when an ApplicationContext is refreshed.\n" +
                    "\n" +
                    "The ContextRefreshedEvent comes from Spring directly and not from Spring Boot and does not extend SpringApplicationEvent.");
        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
