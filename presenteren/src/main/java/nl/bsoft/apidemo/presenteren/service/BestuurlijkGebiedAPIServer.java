package nl.bsoft.apidemo.presenteren.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.mapper.GeoMapper;
import nl.bsoft.apidemo.library.mapper.GeoMapperImpl;
import nl.bsoft.apidemo.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.apidemo.library.model.dto.LocatieDto;
import nl.bsoft.apidemo.library.repository.BestuurlijkGebiedRepository;
import nl.bsoft.apidemo.library.repository.LocatieRepository;
import nl.bsoft.apidemo.presenteren.domain.BestuurlijkGebied;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.wololo.geojson.GeoJSON;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BestuurlijkGebiedAPIServer {

    private final BestuurlijkGebiedRepository bestuurlijkGebiedRepository;
    private final LocatieRepository locatieRepository;
    private GeoMapper geoMapper = new GeoMapperImpl();

    @Autowired
    BestuurlijkGebiedAPIServer(BestuurlijkGebiedRepository bestuurlijkGebiedRepository, LocatieRepository locatieRepository) {
        this.bestuurlijkGebiedRepository = bestuurlijkGebiedRepository;
        this.locatieRepository = locatieRepository;
    }

    public Iterable<BestuurlijkGebied> getBestuurlijkgebieden(PageRequest pageRequest, LocalDate validAt) {
        List<BestuurlijkGebiedDto> bestuurlijkGebiedDtoList = new ArrayList<BestuurlijkGebiedDto>();
        Iterable<BestuurlijkGebied> bestuurlijkgebiedList = new ArrayList<BestuurlijkGebied>();

        bestuurlijkGebiedRepository.findBestuurlijkGebiedActueel(pageRequest, validAt).forEach(bestuurlijkGebiedDtoList::add);

        bestuurlijkGebiedDtoList.forEach(
                bestuurlijkGebiedDto -> {
                    Optional<LocatieDto> locatieDto = locatieRepository.findByMd5hash(bestuurlijkGebiedDto.getMd5hash());
                    GeoJSON geometry = null;
                    if (locatieDto.isPresent()) {
                        geometry = geoMapper.geoJTSToJson(locatieDto.get().getGeometrie());
                    }
                    BestuurlijkGebied bestuurlijkGebied = new BestuurlijkGebied(bestuurlijkGebiedDto);
                    bestuurlijkGebied.setGeometrie(geometry);
                    ((ArrayList<BestuurlijkGebied>) bestuurlijkgebiedList).add(bestuurlijkGebied);
                }
        );

        return bestuurlijkgebiedList;
    }

    public Iterable<BestuurlijkGebied> getBestuurlijkgebied(String identificatie, LocalDate validAt) {
        List<BestuurlijkGebiedDto> bestuurlijkGebiedDtoList = new ArrayList<BestuurlijkGebiedDto>();
        Iterable<BestuurlijkGebied> bestuurlijkgebiedList = new ArrayList<BestuurlijkGebied>();

        bestuurlijkGebiedRepository.findBestuurlijkGebiedDtoByIdentificatieActueel(identificatie, validAt).forEach(bestuurlijkGebiedDtoList::add);

        bestuurlijkGebiedDtoList.forEach(
                bestuurlijkGebiedDto -> {
                    Optional<LocatieDto> locatieDto = locatieRepository.findByMd5hash(bestuurlijkGebiedDto.getMd5hash());
                    GeoJSON geometry = null;
                    if (locatieDto.isPresent()) {
                       geometry = geoMapper.geoJTSToJson(locatieDto.get().getGeometrie());
                    }
                    BestuurlijkGebied bestuurlijkGebied = new BestuurlijkGebied(bestuurlijkGebiedDto);
                    bestuurlijkGebied.setGeometrie(geometry);
                    ((ArrayList<BestuurlijkGebied>) bestuurlijkgebiedList).add(bestuurlijkGebied);
                }
        );

        return bestuurlijkgebiedList;
    }
}
