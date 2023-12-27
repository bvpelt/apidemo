package nl.bsoft.apidemo.library.service;

import lombok.RequiredArgsConstructor;
import nl.bsoft.apidemo.library.mapper.GeoMapper;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;
import org.wololo.geojson.GeoJSON;

@Service
@RequiredArgsConstructor
public class GeoService {
    private final GeoMapper geoMapper;

    public Geometry geoJsonToJTS(GeoJSON geometry) {
        return geoMapper.geoJsonToJTS(geometry);
    }
}