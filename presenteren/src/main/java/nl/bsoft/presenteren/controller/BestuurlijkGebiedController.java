package nl.bsoft.presenteren.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import nl.bsoft.bestuurlijkegrenzen.generated.model.BestuurlijkeGebiedenGet200Response;
import nl.bsoft.bestuurlijkegrenzen.generated.model.Error;
import nl.bsoft.bestuurlijkegrenzen.generated.model.ExtendedError;
import nl.bsoft.presenteren.domain.BestuurlijkGebied;
import nl.bsoft.presenteren.service.BestuurlijkGebiedAPIServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BestuurlijkGebiedController {

    private BestuurlijkGebiedAPIServer bestuurlijkGebiedAPIServer;

    @Autowired
    public BestuurlijkGebiedController(BestuurlijkGebiedAPIServer bestuurlijkGebiedAPIServer) {
        this.bestuurlijkGebiedAPIServer = bestuurlijkGebiedAPIServer;
    }

    @Operation(
            operationId = "bestuurlijkeGebiedenGet",
            summary = "Collectie of all bestuurlijkegebieden",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = BestuurlijkGebied.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad request. Je request body bevat geen geldige JSON of de query wordt niet ondersteund door de API.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedError.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ExtendedError.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized. Je hebt waarschijnlijk geen geldige `X-Api-Key` header meegestuurd.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden. Je hebt geen rechten om deze URL te benaderen.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "406", description = "Not Acceptable. Je hebt waarschijnlijk een gewenst formaat met de `Accept` header verstuurd welke niet ondersteund wordt. De API kan momenteel alleen `application/json` terugsturen.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "503", description = "Service Unavailable. Er vindt mogelijk (gepland) onderhoud of een storing plaats.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    })
            })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/bestuurlijkegebieden",
            produces = { "application/json", "application/problem+json" }
    )
    public ResponseEntity<Iterable<BestuurlijkGebied>> getBestuurlijkgebieden(@RequestParam(value = "pageNumber", defaultValue = "0", required = true) int pageNumber,
                                                                              @RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize,
                                                                              @RequestParam(value = "sortedBy", defaultValue = "identificatie", required = true) String sortBy,
                                                                              @RequestParam("validAt") @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validAt) {


        log.info("BestuurlijkgebiedAPI - pageNumber: {}, pageSize: {}, sortBy: {}", pageNumber, pageSize, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());

        if  (validAt == null) {
            validAt = LocalDate.now();
        }

        log.info("Starting query with parameters - pageNumber: {}, pageSize {}, sortedBy: {}, validAt: {}", pageNumber, pageSize, sortBy, validAt);

        Iterable<BestuurlijkGebied> bestuurlijkgebiedList = bestuurlijkGebiedAPIServer.getBestuurlijkgebieden(pageRequest, validAt);

        return ResponseEntity.ok(bestuurlijkgebiedList);
    }

    @Operation(
            operationId = "bestuurlijkeGebiedenIdentificatieGet",
            summary = "Collectie of all bestuurlijkegebieden",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = BestuurlijkGebied.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad request. Je request body bevat geen geldige JSON of de query wordt niet ondersteund door de API.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedError.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ExtendedError.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized. Je hebt waarschijnlijk geen geldige `X-Api-Key` header meegestuurd.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden. Je hebt geen rechten om deze URL te benaderen.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "406", description = "Not Acceptable. Je hebt waarschijnlijk een gewenst formaat met de `Accept` header verstuurd welke niet ondersteund wordt. De API kan momenteel alleen `application/hal+json` terugsturen.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "503", description = "Service Unavailable. Er vindt mogelijk (gepland) onderhoud of een storing plaats.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    })
            })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/bestuurlijkegebieden/{identificatie}",
            produces = { "application/json", "application/problem+json" }
    )
    public ResponseEntity<Iterable<BestuurlijkGebied>> getBestuurlijkgebied(@PathVariable("identificatie") String identificatie,
                                                                            @RequestParam("validAt") @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validAt) {

        if  (validAt == null) {
            validAt = LocalDate.now();
        }

        log.info("Starting query with parameters - identificatie: {}, validAt: {}", identificatie, validAt);
        Iterable<BestuurlijkGebied> bestuurlijkgebiedList = bestuurlijkGebiedAPIServer.getBestuurlijkgebied(identificatie, validAt);

        return ResponseEntity.ok(bestuurlijkgebiedList);
    }

}
