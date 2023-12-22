package nl.bsoft.synchroniseren.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import nl.bsoft.library.mapper.GeoMapper;
import nl.bsoft.library.mapper.GeoMapperImpl;
import nl.bsoft.library.model.dto.BestuurlijkGebiedDto;
import org.wololo.geojson.GeoJSON;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class BestuurlijkGebied implements Serializable {

    private Long id;

    private String identificatie;

    private String domein;

    private String type;

    private GeoJSON geometrie;

    private LocalDate beginGeldigheid;

    private LocalDate eindGeldigheid;

    private LocalDateTime beginRegistratie;

    private LocalDateTime eindRegistratie;

    @JsonIgnore
    private GeoMapper geoMapper = new GeoMapperImpl();

    public BestuurlijkGebied(BestuurlijkGebiedDto bron) {
        this.id = bron.getId();
        this.identificatie = bron.getIdentificatie();
        this.domein = bron.getDomein();
        this.type = bron.getType();
        this.geometrie = geoMapper.geoJTSToJson(bron.getGeometrie());
        this.beginGeldigheid = bron.getBeginGeldigheid();
        this.eindGeldigheid = bron.getEindGeldigheid();
        this.beginRegistratie = bron.getBeginRegistratie();
        this.eindRegistratie = bron.getEindRegistratie();
    }

}
