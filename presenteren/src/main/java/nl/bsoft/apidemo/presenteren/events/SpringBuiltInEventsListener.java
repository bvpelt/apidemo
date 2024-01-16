package nl.bsoft.apidemo.presenteren.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.*;
import org.springframework.context.ApplicationListener;

@Slf4j
public class SpringBuiltInEventsListener implements ApplicationListener<SpringApplicationEvent> {

    @Override
    public void onApplicationEvent(SpringApplicationEvent event) {
        log.info("Event: =========>>>> {}", event.toString());

        if (event instanceof ApplicationStartingEvent) {
            log.info("Event type: ApplicationStartingEvent\nAn ApplicationStartingEvent is fired at the start of a run but before any processing, except for the registration of listeners and initializers.");
            return;
        }

        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            log.info("Event type: ApplicationEnvironmentPreparedEvent\nAn ApplicationEnvironmentPreparedEvent is fired when the Environment to be used in the context is available.\n\nSince the Environment will be ready at this point, we can inspect and do modify it before it’s used by other beans.");
            return;
        }

        if (event instanceof ApplicationContextInitializedEvent) {
            log.info("Event type: ApplicationContextInitializedEvent\nAn ApplicationContextInitializedEvent is fired when the ApplicationContext is ready and ApplicationContextInitializers are called but bean definitions are not yet loaded.\n" +
                    "\n" +
                    "We can use this to perform a task before beans are initialized into Spring container.");
            return;
        }

        if (event instanceof ApplicationPreparedEvent) {
            log.info("Event type: ApplicationPreparedEvent\nAn ApplicationPreparedEvent is fired when ApplicationContext is prepared but not refreshed.\n" +
                    "\n" +
                    "The Environment is ready for use and bean definitions will be loaded.");
            return;
        }

        if (event instanceof ApplicationStartedEvent) {
            log.info("Event type: ApplicationStartedEvent\nAn ApplicationStartedEvent is fired after the context has been refreshed but before any application and command-line runners have been called.");
            return;
        }

        if (event.getSource() instanceof ApplicationReadyEvent) {
            log.info("Event type: ApplicationReadyEvent\nAn ApplicationReadyEvent is fired to indicate that the application is ready to service requests.\n" +
                    "\n" +
                    "It is advised not to modify the internal state at this point since all initialization steps will be completed.");
            return;
        }

        if (event.getSource() instanceof ApplicationFailedEvent) {
            log.info("Event type: ApplicationFailedEvent\nAn ApplicationFailedEvent is fired if there is an exception and the application fails to start. This can happen at any time during startup.\n" +
                    "\n" +
                    "We can use this to perform some tasks like execute a script or notify on startup failure.");
            return;
        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
