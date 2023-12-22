package nl.bsoft.synchroniseren.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.library.model.dto.OpenbaarLichaamDto;
import nl.bsoft.synchroniseren.service.OpenbaarLichaamAPIServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OpenbaarLichaamController {

    private OpenbaarLichaamAPIServer openbaarLichaamAPIServer;

    @Autowired
    public OpenbaarLichaamController(OpenbaarLichaamAPIServer openbaarLichaamAPIServer) {
        this.openbaarLichaamAPIServer = openbaarLichaamAPIServer;
    }

    @GetMapping("/api/openbarelichamen")
    public ResponseEntity<Iterable<OpenbaarLichaamDto>> getOpenbareLichamen(@RequestParam(value = "pageNumber", defaultValue = "0", required = true) int pageNumber,
                                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize,
                                                                            @RequestParam(value = "sortedBy", defaultValue = "code", required = true) String sortBy) {

        log.info("OpenbaarLichaamAPI - pageNumber: {}, pageSize: {}, sortBy: {}", pageNumber, pageSize, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());

        Iterable<OpenbaarLichaamDto> openbaarLichaamDtos = openbaarLichaamAPIServer.getOpenbareLichamen(pageRequest);

        return ResponseEntity.ok(openbaarLichaamDtos);
    }

    @GetMapping("/api/openbarelichamen/{identificatie}")
    public ResponseEntity<Iterable<OpenbaarLichaamDto>> getBestuurlijkgebied(@PathVariable("identificatie") String code) {

        Iterable<OpenbaarLichaamDto> openbaarLichaamDtos = openbaarLichaamAPIServer.getOpenbareLichaam(code);

        return ResponseEntity.ok(openbaarLichaamDtos);
    }

}
