package nl.bsoft.apidemo.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.model.dto.OpenbaarLichaamDto;
import nl.bsoft.apidemo.library.repository.OpenbaarLichaamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OpenbaarLichaamStorageService {

    private final OpenbaarLichaamRepository openbaarLichaamRepository;

    @Autowired
    public OpenbaarLichaamStorageService(OpenbaarLichaamRepository openbaarLichaamRepository) {
        this.openbaarLichaamRepository = openbaarLichaamRepository;
    }

    public OpenbaarLichaamDto Save(OpenbaarLichaamDto openbaarLichaamDto) {
        log.debug("Saving {}", openbaarLichaamDto.toString());
        OpenbaarLichaamDto savedOpenbaarLichaamDto = openbaarLichaamRepository.save(openbaarLichaamDto);

        return savedOpenbaarLichaamDto;
    }

    public OpenbaarLichaamDto SaveWithHistory(OpenbaarLichaamDto original, OpenbaarLichaamDto last) {
        log.debug("Saving with history original: {}\n, last: {}", original.toString(),  last.toString());
        OpenbaarLichaamDto savedOriginalDto = openbaarLichaamRepository.save(original);
        OpenbaarLichaamDto savedLastDto = openbaarLichaamRepository.save(last);

        return savedLastDto;
    }

    public List<OpenbaarLichaamDto> findByIdentificatie(String code) {
        return openbaarLichaamRepository.findOpenbaarLichaamDtoByCode(code);
    }
}
