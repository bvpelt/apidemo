package nl.bsoft.presenteren.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.library.model.dto.OpenbaarLichaamDto;

import nl.bsoft.presenteren.service.OpenbaarLichaamAPIServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OpenbaarLichaamController {

    private OpenbaarLichaamAPIServer openbaarLichaamAPIServer;

    @Autowired
    public OpenbaarLichaamController(OpenbaarLichaamAPIServer openbaarLichaamAPIServer) {
        this.openbaarLichaamAPIServer = openbaarLichaamAPIServer;
    }
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/openbarelichamen",
            produces = { "application/hal+json", "application/problem+json" }
    )
    public ResponseEntity<Iterable<OpenbaarLichaamDto>> getOpenbareLichamen(@RequestParam(value = "pageNumber", defaultValue = "0", required = true) int pageNumber,
                                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize,
                                                                            @RequestParam(value = "sortedBy", defaultValue = "code", required = true) String sortBy) {

        log.info("OpenbaarLichaamAPI - pageNumber: {}, pageSize: {}, sortBy: {}", pageNumber, pageSize, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());

        Iterable<OpenbaarLichaamDto> openbaarLichaamDtos = openbaarLichaamAPIServer.getOpenbareLichamen(pageRequest);

        return ResponseEntity.ok(openbaarLichaamDtos);
    }
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/openbarelichamen/{identificatie}",
            produces = { "application/hal+json", "application/problem+json" }
    )
    public ResponseEntity<Iterable<OpenbaarLichaamDto>> getBestuurlijkgebied(@PathVariable("identificatie") String code) {

        Iterable<OpenbaarLichaamDto> openbaarLichaamDtos = openbaarLichaamAPIServer.getOpenbareLichaam(code);

        return ResponseEntity.ok(openbaarLichaamDtos);
    }

}
