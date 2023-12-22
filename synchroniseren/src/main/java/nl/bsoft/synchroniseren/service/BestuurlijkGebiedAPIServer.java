package nl.bsoft.synchroniseren.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.library.repository.BestuurlijkGebiedRepository;
import nl.bsoft.synchroniseren.domain.BestuurlijkGebied;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BestuurlijkGebiedAPIServer {

    private final BestuurlijkGebiedRepository bestuurlijkGebiedRepository;

    @Autowired
    BestuurlijkGebiedAPIServer(BestuurlijkGebiedRepository bestuurlijkGebiedRepository) {
        this.bestuurlijkGebiedRepository = bestuurlijkGebiedRepository;
    }

    public Iterable<BestuurlijkGebied> getBestuurlijkgebieden(PageRequest pageRequest) {
        List<BestuurlijkGebiedDto> bestuurlijkGebiedDtoList = new ArrayList<BestuurlijkGebiedDto>();
        Iterable<BestuurlijkGebied> bestuurlijkgebiedList = new ArrayList<BestuurlijkGebied>();


        LocalDate peilmoment = LocalDate.now();

        // bestuurlijkGebiedRepository.findAll(pageRequest).forEach(bestuurlijkGebiedDtoList::add);
        bestuurlijkGebiedRepository.findBestuurlijkGebiedActueel(pageRequest, peilmoment).forEach(bestuurlijkGebiedDtoList::add);

        bestuurlijkGebiedDtoList.forEach(
                bestuurlijkGebiedDto -> {
                    ((ArrayList<BestuurlijkGebied>) bestuurlijkgebiedList).add(new BestuurlijkGebied(bestuurlijkGebiedDto));
                }
        );

        return bestuurlijkgebiedList;
    }

    public Iterable<BestuurlijkGebied> getBestuurlijkgebied(String identificatie) {
        List<BestuurlijkGebiedDto> bestuurlijkGebiedDtoList = new ArrayList<BestuurlijkGebiedDto>();
        Iterable<BestuurlijkGebied> bestuurlijkgebiedList = new ArrayList<BestuurlijkGebied>();

        LocalDate peilmoment = LocalDate.now();

        bestuurlijkGebiedRepository.findBestuurlijkGebiedDtoByIdentificatieActueel(identificatie, peilmoment).forEach(bestuurlijkGebiedDtoList::add);

        bestuurlijkGebiedDtoList.forEach(
                bestuurlijkGebiedDto -> {
                    ((ArrayList<BestuurlijkGebied>) bestuurlijkgebiedList).add(new BestuurlijkGebied(bestuurlijkGebiedDto));
                }
        );

        return bestuurlijkgebiedList;
    }
}
