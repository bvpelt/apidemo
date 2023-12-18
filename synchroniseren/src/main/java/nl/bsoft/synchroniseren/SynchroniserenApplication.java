package nl.bsoft.synchroniseren;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"nl.bsoft.library"})
@SpringBootApplication
public class SynchroniserenApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynchroniserenApplication.class, args);
    }

}
