package nl.bsoft.presenteren;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"nl.bsoft.library", "nl.bsoft.bestuurlijkegrenzen.generated.model"})
@EnableJpaRepositories(basePackages = "nl.bsoft.library")
@SpringBootApplication
public class PresenterenApplication {

    public static void main(String[] args) {
        SpringApplication.run(PresenterenApplication.class, args);
    }

}
