package nl.bsoft.apidemo.presenteren.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class SpringBuiltInWebserverEventsListener implements ApplicationListener<WebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        log.info("Event: =========>>>> {}", event.toString());

        if (event instanceof WebServerInitializedEvent) {
            log.info("Event type: WebServerInitializedEvent\nIf we’re using a web server, a WebServerInitializedEvent is fired after the web server is ready. ServletWebServerInitializedEvent and ReactiveWebServerInitializedEvent are the servlet and reactive variants, respectively.\n" +
                    "\n" +
                    "The WebServerInitializedEvent does not extend SpringApplicationEvent.");
            return;
        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
