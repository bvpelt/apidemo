package nl.bsoft.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.library.model.dto.OpenbaarLichaamDto;
import nl.bsoft.library.repository.OpenbaarLichaamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OpenbaarLichaamStorageService {

    private OpenbaarLichaamRepository openbaarLichaamRepository;

    @Autowired
    public OpenbaarLichaamStorageService(OpenbaarLichaamRepository openbaarLichaamRepository) {
        this.openbaarLichaamRepository = openbaarLichaamRepository;
    }

    public OpenbaarLichaamDto Save(OpenbaarLichaamDto openbaarLichaamDto) {
        log.debug("Saving {}", openbaarLichaamDto.toString());
        OpenbaarLichaamDto savedOpenbaarLichaamDto = openbaarLichaamRepository.save(openbaarLichaamDto);

        return savedOpenbaarLichaamDto;
    }

    public List<OpenbaarLichaamDto> findByIdentificatie(String code) {
        return openbaarLichaamRepository.findOpenbaarLichaamDtoByCode(code);
    }
}
