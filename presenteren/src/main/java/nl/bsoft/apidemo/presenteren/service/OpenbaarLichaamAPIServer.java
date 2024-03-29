package nl.bsoft.apidemo.presenteren.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.model.dto.OpenbaarLichaamDto;
import nl.bsoft.apidemo.library.repository.OpenbaarLichaamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OpenbaarLichaamAPIServer {

    private final OpenbaarLichaamRepository openbaarLichaamRepository;

    @Autowired
    OpenbaarLichaamAPIServer(OpenbaarLichaamRepository openbaarLichaamRepository) {
        this.openbaarLichaamRepository = openbaarLichaamRepository;
    }

    public Iterable<OpenbaarLichaamDto> getOpenbareLichamen(LocalDateTime validAt, PageRequest pageRequest) {
        Iterable<OpenbaarLichaamDto> openbaarLichaamDtoList = new ArrayList<OpenbaarLichaamDto>();

        openbaarLichaamDtoList = openbaarLichaamRepository.findOpenbaarLichamenDtoByCodeAtDate(validAt, pageRequest);
        return openbaarLichaamDtoList;
    }

    public List<OpenbaarLichaamDto> getOpenbareLichaam(String code) {
        List<OpenbaarLichaamDto> openbaarLichaamDtoList = new ArrayList<OpenbaarLichaamDto>();

        openbaarLichaamDtoList = openbaarLichaamRepository.findOpenbaarLichaamDtoByCode(code);

        return openbaarLichaamDtoList;
    }

    public List<OpenbaarLichaamDto> getOpenbareLichaamAtDate(LocalDateTime validAt, String code) {
        List<OpenbaarLichaamDto> openbaarLichaamDtoList = new ArrayList<OpenbaarLichaamDto>();

        openbaarLichaamDtoList = openbaarLichaamRepository.findOpenbaarLichaamDtoByCodeAtDate(validAt, code);

        return openbaarLichaamDtoList;
    }
}
