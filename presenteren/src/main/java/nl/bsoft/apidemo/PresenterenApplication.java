package nl.bsoft.apidemo;

import nl.bsoft.apidemo.presenteren.events.SpringBuiltInContextEventsListener;
import nl.bsoft.apidemo.presenteren.events.SpringBuiltInEventsListener;
import nl.bsoft.apidemo.presenteren.events.SpringBuiltInWebserverEventsListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"nl.bsoft.apidemo.library", "nl.bsoft.bestuurlijkegrenzen.generated.model"})
@EnableJpaRepositories(basePackages = "nl.bsoft.apidemo.library")
@SpringBootApplication
public class PresenterenApplication {

    public static void main(String[] args) {

        SpringApplication springApplication =
                new SpringApplication(PresenterenApplication.class);
        springApplication.addListeners(new SpringBuiltInEventsListener());
        springApplication.addListeners(new SpringBuiltInContextEventsListener());
        springApplication.addListeners(new SpringBuiltInWebserverEventsListener());
        springApplication.run(args);
    }

}
