package nl.bsoft.apidemo.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.apidemo.library.model.dto.LocatieDto;
import nl.bsoft.apidemo.library.repository.BestuurlijkGebiedRepository;
import nl.bsoft.apidemo.library.repository.LocatieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LocatieStorageService {

    private final LocatieRepository locatieRepository;

    @Autowired
    public LocatieStorageService(LocatieRepository locatieRepository) {
        this.locatieRepository = locatieRepository;
    }

    public LocatieDto save(LocatieDto locatieDto) {
        log.debug("Saving {}", locatieDto.toString());
        LocatieDto savedLocatieDto = locatieRepository.save(locatieDto);

        return savedLocatieDto;
    }

    public LocatieDto saveOrUpdate(LocatieDto locatieDto) {
        log.debug("SaveOrUpdate {}", locatieDto.toString());
        Optional<LocatieDto> foundLocatieDto = locatieRepository.findByMd5hash(locatieDto.getMd5hash());
        LocatieDto savedLocatieDto = null;

        if (foundLocatieDto.isPresent()) {
            savedLocatieDto = foundLocatieDto.get();
        } else {
            savedLocatieDto = save(locatieDto);
        }

        return savedLocatieDto;
    }

}
