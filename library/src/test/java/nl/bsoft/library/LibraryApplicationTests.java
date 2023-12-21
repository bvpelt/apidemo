package nl.bsoft.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkGebied;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@SpringBootTest
class LibraryApplicationTests {

    @Autowired
    private ResourceLoader resourceLoader = null;

    @Test
    void contextLoads() {
    }

    @Test
    void datumTijd() {
        LocalDate date = LocalDate.now();
        log.info("Data: {}", date);

        LocalDateTime time = LocalDateTime.now();
        log.info("Time: {}", time);

        time = LocalDateTime.now(ZoneId.of("UTC"));
        log.info("Time: {}", time);
    }

    @Test
    public void mapBestuurlijkGebiedtoDto() {
        BestuurlijkGebied bestuurlijkGebied;

        try {
            File dataFile = resourceLoader.getResource("classpath:input.json").getFile();

            ObjectMapper objectMapper = new ObjectMapper();
            bestuurlijkGebied = objectMapper.readValue(dataFile, BestuurlijkGebied.class);
            log.info("bestuurlijkgebied: {}", bestuurlijkGebied.toString());
        } catch (Exception e) {
            log.error("Error occured: {}", e.getMessage());
            //throw new RuntimeException(e);
        }
    }
}
