package nl.bsoft.apidemo.library.mapper;

import lombok.Setter;
import nl.bsoft.apidemo.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.apidemo.library.model.dto.LocatieDto;
import nl.bsoft.apidemo.library.service.GeoService;
import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkGebied;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.wololo.geojson.GeoJSON;

@Setter
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                JsonNullableMapper.class
        },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class LocatieMapper {
    @Mapping(target = "geometrie", source = "geometrie", qualifiedByName = "toGeometrie")
    public abstract LocatieDto toLocatieDto(BestuurlijkGebied bestuurlijkGebied) throws ParseException;

    @Named("toGeometrie")
    protected Geometry toGeometrie(GeoJSON inputGeometry) throws ParseException {

        GeoService geoService = new GeoService(new GeoMapperImpl());
        return geoService.geoJsonToJTS(inputGeometry);
    }
}
