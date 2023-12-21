package nl.bsoft.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.library.repository.BestuurlijkGebiedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BestuurlijkeGebiedenStorageService {

    private final BestuurlijkGebiedRepository bestuurlijkGebiedRepository;

    @Autowired
    public BestuurlijkeGebiedenStorageService(BestuurlijkGebiedRepository bestuurlijkGebiedRepository) {
        this.bestuurlijkGebiedRepository = bestuurlijkGebiedRepository;
    }

    public BestuurlijkGebiedDto Save(BestuurlijkGebiedDto bestuurlijkGebiedDto) {
        log.debug("Saving {}", bestuurlijkGebiedDto.toString());
        BestuurlijkGebiedDto savedBestuurlijkGebiedDto = bestuurlijkGebiedRepository.save(bestuurlijkGebiedDto);

        return savedBestuurlijkGebiedDto;
    }

    public BestuurlijkGebiedDto SaveWithHistory(BestuurlijkGebiedDto original, BestuurlijkGebiedDto copy, BestuurlijkGebiedDto last) {
        log.debug("Saving with history original: {}\n copy: {}\n, last: {}", original.toString(), copy.toString(), last.toString());
        BestuurlijkGebiedDto savedOriginalDto = bestuurlijkGebiedRepository.save(original);
        BestuurlijkGebiedDto savedCopyDto = bestuurlijkGebiedRepository.save(copy);
        BestuurlijkGebiedDto savedLastDto = bestuurlijkGebiedRepository.save(last);

        return savedLastDto;
    }

    public List<BestuurlijkGebiedDto> findByIdentificatie(String identificatie) {
        return bestuurlijkGebiedRepository.findBestuurlijkGebiedDtoByIdentificatie(identificatie);
    }
}
