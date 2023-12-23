package nl.bsoft.presenteren.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.library.repository.BestuurlijkGebiedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.bsoft.presenteren.domain.BestuurlijkGebied;
@Slf4j
@Service
public class BestuurlijkGebiedAPIServer {

    private final BestuurlijkGebiedRepository bestuurlijkGebiedRepository;

    @Autowired
    BestuurlijkGebiedAPIServer(BestuurlijkGebiedRepository bestuurlijkGebiedRepository) {
        this.bestuurlijkGebiedRepository = bestuurlijkGebiedRepository;
    }

    public Iterable<BestuurlijkGebied> getBestuurlijkgebieden(PageRequest pageRequest, LocalDate validAt) {
        List<BestuurlijkGebiedDto> bestuurlijkGebiedDtoList = new ArrayList<BestuurlijkGebiedDto>();
        Iterable<BestuurlijkGebied> bestuurlijkgebiedList = new ArrayList<BestuurlijkGebied>();

        bestuurlijkGebiedRepository.findBestuurlijkGebiedActueel(pageRequest, validAt).forEach(bestuurlijkGebiedDtoList::add);

        bestuurlijkGebiedDtoList.forEach(
                bestuurlijkGebiedDto -> {
                    ((ArrayList<BestuurlijkGebied>) bestuurlijkgebiedList).add(new BestuurlijkGebied(bestuurlijkGebiedDto));
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
                    ((ArrayList<BestuurlijkGebied>) bestuurlijkgebiedList).add(new BestuurlijkGebied(bestuurlijkGebiedDto));
                }
        );

        return bestuurlijkgebiedList;
    }
}
