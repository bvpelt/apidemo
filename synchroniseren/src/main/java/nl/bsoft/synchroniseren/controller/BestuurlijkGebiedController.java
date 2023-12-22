package nl.bsoft.synchroniseren.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.synchroniseren.domain.BestuurlijkGebied;
import nl.bsoft.synchroniseren.service.BestuurlijkGebiedAPIServer;
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
public class BestuurlijkGebiedController {

    private BestuurlijkGebiedAPIServer bestuurlijkGebiedAPIServer;

    @Autowired
    public BestuurlijkGebiedController(BestuurlijkGebiedAPIServer bestuurlijkGebiedAPIServer) {
        this.bestuurlijkGebiedAPIServer = bestuurlijkGebiedAPIServer;
    }

    @GetMapping("/api/bestuurlijkegebieden")
    public ResponseEntity<Iterable<BestuurlijkGebied>> getBestuurlijkgebieden(@RequestParam(value = "pageNumber", defaultValue = "0", required = true) int pageNumber,
                                                                              @RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize,
                                                                              @RequestParam(value = "sortedBy", defaultValue = "identificatie", required = true) String sortBy) {

        log.info("BestuurlijkgebiedAPI - pageNumber: {}, pageSize: {}, sortBy: {}", pageNumber, pageSize, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());

        Iterable<BestuurlijkGebied> bestuurlijkgebiedList = bestuurlijkGebiedAPIServer.getBestuurlijkgebieden(pageRequest);

        return ResponseEntity.ok(bestuurlijkgebiedList);
    }

    @GetMapping("/api/bestuurlijkegebieden/{identificatie}")
    public ResponseEntity<Iterable<BestuurlijkGebied>> getBestuurlijkgebied(@PathVariable("identificatie") String identificatie) {

        Iterable<BestuurlijkGebied> bestuurlijkgebiedList = bestuurlijkGebiedAPIServer.getBestuurlijkgebied(identificatie);

        return ResponseEntity.ok(bestuurlijkgebiedList);
    }

}
