package nl.bsoft.apidemo.presenteren.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.presenteren.domain.BestuurlijkGebied;
import nl.bsoft.apidemo.library.model.dto.OpenbaarLichaamDto;
import nl.bsoft.apidemo.presenteren.service.OpenbaarLichaamAPIServer;
import nl.bsoft.bestuurlijkegrenzen.generated.model.Error;
import nl.bsoft.bestuurlijkegrenzen.generated.model.ExtendedError;
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

    @Operation(
            operationId = "openbareLichamenGet",
            summary = "Collectie of all openbarelichamen",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/hal+json", schema = @Schema(implementation = BestuurlijkGebied.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad request. Je request body bevat geen geldige JSON of de query wordt niet ondersteund door de API.", content = {
                            @Content(mediaType = "application/hal+json", schema = @Schema(implementation = ExtendedError.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ExtendedError.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized. Je hebt waarschijnlijk geen geldige `X-Api-Key` header meegestuurd.", content = {
                            @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden. Je hebt geen rechten om deze URL te benaderen.", content = {
                            @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "406", description = "Not Acceptable. Je hebt waarschijnlijk een gewenst formaat met de `Accept` header verstuurd welke niet ondersteund wordt. De API kan momenteel alleen `application/json` terugsturen.", content = {
                            @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "503", description = "Service Unavailable. Er vindt mogelijk (gepland) onderhoud of een storing plaats.", content = {
                            @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Error.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    })
            })
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/openbarelichamen",
            produces = {"application/hal+json", "application/problem+json"}
    )
    public ResponseEntity<Iterable<OpenbaarLichaamDto>> getOpenbareLichamen(@RequestParam(value = "pageNumber", defaultValue = "0", required = true) int pageNumber,
                                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize,
                                                                            @RequestParam(value = "sortedBy", defaultValue = "code", required = true) String sortBy) {

        log.info("OpenbaarLichaamAPI - pageNumber: {}, pageSize: {}, sortBy: {}", pageNumber, pageSize, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());

        Iterable<OpenbaarLichaamDto> openbaarLichaamDtos = openbaarLichaamAPIServer.getOpenbareLichamen(pageRequest);

        return ResponseEntity.ok(openbaarLichaamDtos);
    }

    @Operation(
            operationId = "openbareLichamenGet",
            summary = "Collectie of all openbarelichamen",
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
            value = "/api/openbarelichamen/{identificatie}",
            produces = {"application/json", "application/problem+json"}
    )
    public ResponseEntity<Iterable<OpenbaarLichaamDto>> getBestuurlijkgebied(@PathVariable("identificatie") String code) {
        log.info("OpenbaarLichaamAPI - identificatie: {}", code);

        Iterable<OpenbaarLichaamDto> openbaarLichaamDtos = openbaarLichaamAPIServer.getOpenbareLichaam(code);

        return ResponseEntity.ok(openbaarLichaamDtos);
    }

}
