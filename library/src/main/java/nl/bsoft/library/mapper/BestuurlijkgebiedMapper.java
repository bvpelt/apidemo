package nl.bsoft.library.mapper;

import lombok.Setter;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkGebied;
import nl.bsoft.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.library.service.GeoService;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;
import org.wololo.geojson.GeoJSON;

import java.time.LocalDate;

@Setter
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                JsonNullableMapper.class
        },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class BestuurlijkgebiedMapper {

    //@Autowired
    //private WKTReader wktReader;


    @Mapping(target = "identificatie", source = "identificatie")
    @Mapping(target = "domein", source = "domein")
    @Mapping(target = "type", source = "type", qualifiedByName = "toType")
    @Mapping(target = "beginGeldigheid", source = "embedded.metadata.beginGeldigheid", qualifiedByName = "toLocalDate")
    @Mapping(target = "eindGeldigheid", source = "embedded.metadata.eindGeldigheid", qualifiedByName = "toLocalDate")
    @Mapping(target = "geometrie", source = "geometrie", qualifiedByName = "toGeometrie")
    public abstract BestuurlijkGebiedDto toBestuurlijkgeBiedDto(BestuurlijkGebied bestuurlijkGebied) throws ParseException;


    @Named("toType")
    protected String toType(BestuurlijkGebied.TypeEnum type) {
        return type.getValue();
    }

    @Named("toGeometrie")
    protected Geometry toGeometrie(GeoJSON inputGeometry) throws ParseException {

        GeoService geoService = new GeoService(new GeoMapperImpl());
        //Geometry geometry = wktReader.read(inputGeometry.toString());
        return geoService.geoJsonToJTS(inputGeometry);


        //return geometry;
    }

    @Named("toLocalDate")
    protected LocalDate toLocalDate(JsonNullable<LocalDate> value) {
        LocalDate localDate = null;

        if (value.isPresent()) {
            localDate = value.get();
        }
        return localDate;
    }
}
