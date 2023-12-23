package nl.bsoft.library.mapper;

import lombok.Setter;
import nl.bsoft.bestuurlijkegrenzen.generated.model.OpenbaarLichaam;
import nl.bsoft.library.model.dto.OpenbaarLichaamDto;
import org.locationtech.jts.io.ParseException;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                JsonNullableMapper.class
        },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class OpenbaarLichaamMapper {

    @Mapping(target = "code", source = "code", qualifiedByName = "toCode")
    @Mapping(target = "oin", source = "oin", qualifiedByName = "toOin")
    @Mapping(target = "type", source = "type", qualifiedByName = "toType")
    @Mapping(target = "naam", source = "naam")
    @Mapping(target = "bestuurslaag", source = "bestuurslaag", qualifiedByName = "toBestuurslaag")
    public abstract OpenbaarLichaamDto toOpenbaarLichaamDto(OpenbaarLichaam openbaarLichaam) throws ParseException;

    @Named("toCode")
    protected String toCode(JsonNullable<String> value) {
        String code = null;

        if (value.isPresent()) {
            code = value.get();
        }
        return code;
    }

    @Named("toOin")
    protected String toOin(JsonNullable<String> value) {
        String oin = null;

        if (value.isPresent()) {
            oin = value.get();
        }
        return oin;
    }

    @Named("toType")
    protected String toType(OpenbaarLichaam.TypeEnum type) {
        return type.getValue();
    }

    @Named("toBestuurslaag")
    protected String toType(OpenbaarLichaam.BestuurslaagEnum bestuurslaag) {
        return bestuurslaag.getValue();
    }

}
