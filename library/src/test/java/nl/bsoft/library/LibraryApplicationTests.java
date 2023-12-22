package nl.bsoft.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkGebied;
import nl.bsoft.library.mapper.BestuurlijkgebiedMapper;
import nl.bsoft.library.mapper.BestuurlijkgebiedMapperImpl;
import nl.bsoft.library.mapper.GeoMapperImpl;
import nl.bsoft.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.library.service.GeoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@SpringBootTest
class LibraryApplicationTests {

    private final BestuurlijkgebiedMapper mapper = new BestuurlijkgebiedMapperImpl();
    @Autowired
    private ResourceLoader resourceLoader = null;
    @Autowired
    private ObjectMapper objectMapper;

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
        GeoService geoService = new GeoService(new GeoMapperImpl());

        try {
            File dataFile = resourceLoader.getResource("classpath:input.json").getFile();

            bestuurlijkGebied = objectMapper.readValue(dataFile, BestuurlijkGebied.class);
            log.info("bestuurlijkgebied: \n{}", bestuurlijkGebied.toString());

            BestuurlijkGebiedDto bestuurlijkGebiedDto = mapper.toBestuurlijkgeBiedDto(bestuurlijkGebied);

            log.info("bestuurlijkgebiedDto: {}", bestuurlijkGebiedDto.toString());

            Assert.notNull(bestuurlijkGebiedDto, "Mapping not successfull");
            Assert.isTrue(bestuurlijkGebied.getIdentificatie().equals(bestuurlijkGebiedDto.getIdentificatie()), "ddentificatie not mapped");
            Assert.isTrue(bestuurlijkGebied.getDomein().equals(bestuurlijkGebiedDto.getDomein()), "domein not mapped");
            Assert.isTrue(bestuurlijkGebied.getType().getValue().equals(bestuurlijkGebiedDto.getType()), "type not mapped");
            Assert.isTrue(bestuurlijkGebied.getEmbedded().getMetadata().getBeginGeldigheid().get().equals(bestuurlijkGebiedDto.getBeginGeldigheid()), "beginGeldigheid not mapped");
            Assert.isTrue(!bestuurlijkGebied.getEmbedded().getMetadata().getEindGeldigheid().isPresent(), "eindgeldigheid not expected");
            Assert.isTrue(geoService.geoJsonToJTS(bestuurlijkGebied.getGeometrie()).equals(bestuurlijkGebiedDto.getGeometrie()), "geometrie not mapped");
        } catch (Exception e) {
            log.error("Error occured: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
