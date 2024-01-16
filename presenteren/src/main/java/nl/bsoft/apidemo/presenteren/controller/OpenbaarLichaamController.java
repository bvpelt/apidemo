package nl.bsoft.apidemo.presenteren.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.model.dto.OpenbaarLichaamDto;
import nl.bsoft.apidemo.presenteren.domain.BestuurlijkGebied;
import nl.bsoft.apidemo.presenteren.service.OpenbaarLichaamAPIServer;
import nl.bsoft.bestuurlijkegrenzen.generated.model.Error;
import nl.bsoft.bestuurlijkegrenzen.generated.model.ExtendedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
    public ResponseEntity<?> getOpenbareLichamen(@RequestParam(value = "page", defaultValue = "0", required = true) int pageNumber,
                                                 @RequestParam(value = "size", defaultValue = "10", required = true) int pageSize,
                                                 @RequestParam(value = "sort", defaultValue = "code,desc", required = true) String[] sortBy,
                                                 @RequestHeader(value = "X-Api-Key") String xapikey) {

        // validate input
        if ((xapikey == null) || (xapikey.length() == 0)) {
            log.warn("No api key specifed: \n{}");
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("No api key specified");
        }

        // get request parameters
        List<Sort.Order> sortParameter = ControllerSortUtil.getSortOrder(sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortParameter));

        log.info("OpenbaarLichaamAPI starting query with parameters - pageNumber: {}, pageSize {}, sortedBy: {}", pageNumber, pageSize, Arrays.toString(sortBy));

        // execute request
        try {
            Iterable<OpenbaarLichaamDto> openbaarLichaamDtos = openbaarLichaamAPIServer.getOpenbareLichamen(pageRequest);
            return ResponseEntity.ok(openbaarLichaamDtos);
        } catch (PropertyReferenceException e) {
            log.warn("Invalid parameters: \n{}", e);
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("Invalid parameters: " + e.getMessage());
        }
    }

    @Operation(
            operationId = "openbareLichamenGet",
            summary = "A specific openbarelichaam",
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
