package nl.bsoft.presenteren.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.library.model.dto.OpenbaarLichaamDto;
import nl.bsoft.library.repository.OpenbaarLichaamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    public Iterable<OpenbaarLichaamDto> getOpenbareLichamen(PageRequest pageRequest) {
        Iterable<OpenbaarLichaamDto> openbaarLichaamDtoList = new ArrayList<OpenbaarLichaamDto>();

        openbaarLichaamDtoList = openbaarLichaamRepository.findAll(pageRequest);
        return openbaarLichaamDtoList;
    }

    public List<OpenbaarLichaamDto> getOpenbareLichaam(String code) {
        List<OpenbaarLichaamDto> openbaarLichaamDtoList = new ArrayList<OpenbaarLichaamDto>();

        openbaarLichaamDtoList = openbaarLichaamRepository.findOpenbaarLichaamDtoByCode(code);

        return openbaarLichaamDtoList;
    }
}
