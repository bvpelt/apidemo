package nl.bsoft.apidemo.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.mapper.*;
import nl.bsoft.apidemo.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.apidemo.library.model.dto.OpenbaarLichaamDto;
import nl.bsoft.apidemo.library.service.GeoService;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkGebied;
import nl.bsoft.bestuurlijkegrenzen.generated.model.OpenbaarLichaam;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@SpringBootTest
class LibraryApplicationTests {

    private final BestuurlijkgebiedMapper bestuurlijkeGebiedMapper = new BestuurlijkgebiedMapperImpl();
    private final OpenbaarLichaamMapper openbaarLichaamMapper = new OpenbaarLichaamMapperImpl();
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
    public void mapBestuurlijkGebiedto() {
        BestuurlijkGebied bestuurlijkGebied;
        GeoService geoService = new GeoService(new GeoMapperImpl());

        try {
            File dataFile = resourceLoader.getResource("classpath:bestuurlijkgebied.json").getFile();

            bestuurlijkGebied = objectMapper.readValue(dataFile, BestuurlijkGebied.class);
            log.info("bestuurlijkgebied: \n{}", bestuurlijkGebied.toString());

            BestuurlijkGebiedDto bestuurlijkGebiedDto = bestuurlijkeGebiedMapper.toBestuurlijkgeBiedDto(bestuurlijkGebied);

            log.info("bestuurlijkgebiedDto: {}", bestuurlijkGebiedDto.toString());

            Assert.notNull(bestuurlijkGebiedDto, "Mapping not successfull");
            Assert.isTrue(bestuurlijkGebied.getIdentificatie().equals(bestuurlijkGebiedDto.getIdentificatie()), "ddentificatie not mapped");
            Assert.isTrue(bestuurlijkGebied.getDomein().equals(bestuurlijkGebiedDto.getDomein()), "domein not mapped");
            Assert.isTrue(bestuurlijkGebied.getType().getValue().equals(bestuurlijkGebiedDto.getType()), "type not mapped");
            Assert.notNull(bestuurlijkGebied.getEmbedded(), "Embedded not included");
            Assert.notNull(bestuurlijkGebied.getEmbedded().getMetadata(), "Metadata not included");
            Assert.isTrue(bestuurlijkGebied.getEmbedded().getMetadata().getBeginGeldigheid().isPresent(), "begin geldigheid not present");
            Assert.isTrue(bestuurlijkGebied.getEmbedded().getMetadata().getBeginGeldigheid().get().equals(bestuurlijkGebiedDto.getBeginGeldigheid()), "beginGeldigheid not mapped");
            Assert.isTrue(!bestuurlijkGebied.getEmbedded().getMetadata().getEindGeldigheid().isPresent(), "eindgeldigheid not expected");
            Assert.isTrue(geoService.geoJsonToJTS(bestuurlijkGebied.getGeometrie()).equals(bestuurlijkGebiedDto.getGeometrie()), "geometrie not mapped");
        } catch (IOException e) {
            log.error("IO Error occured: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (ParseException e) {
            log.error("Parse Error occured: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Test
    public void mapOpenbaarLichaamdto() {
        OpenbaarLichaam openbaarLichaam;
        GeoService geoService = new GeoService(new GeoMapperImpl());

        try {
            File dataFile = resourceLoader.getResource("classpath:openbaarlichaam.json").getFile();

            openbaarLichaam = objectMapper.readValue(dataFile, OpenbaarLichaam.class);
            log.info("openbaarLichaam: \n{}", openbaarLichaam.toString());

            OpenbaarLichaamDto openbaarLichaamDto = openbaarLichaamMapper.toOpenbaarLichaamDto(openbaarLichaam);

            log.info("openbaarLichaamDto: {}", openbaarLichaamDto.toString());

            Assert.notNull(openbaarLichaamDto, "Mapping not successfull");
            Assert.isTrue(openbaarLichaam.getCode().isPresent(), "code not present");
            Assert.isTrue(openbaarLichaam.getCode().get().equals(openbaarLichaamDto.getCode()), "code not mapped");
            Assert.isTrue(openbaarLichaam.getOin().isPresent(), "oin not present");
            Assert.isTrue(openbaarLichaam.getOin().get().equals(openbaarLichaamDto.getOin()), "oin not mapped");
            Assert.isTrue(openbaarLichaam.getType().toString().equals(openbaarLichaamDto.getType()), "type not mapped");
            Assert.isTrue(openbaarLichaam.getNaam().equals(openbaarLichaamDto.getNaam()), "naam not mapped");
            Assert.isTrue(openbaarLichaam.getBestuurslaag().toString().equals(openbaarLichaamDto.getBestuurslaag()), "bestuurslaag not mapped");
        } catch (IOException e) {
            log.error("IO Error occured: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (ParseException e) {
            log.error("Parse Error occured: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
