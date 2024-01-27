package nl.bsoft.apidemo.synchroniseren.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.synchroniseren.service.BestuurlijkeGrenzenProcessingService;
import nl.bsoft.apidemo.synchroniseren.service.OpenbareLichamenProcessingService;
import nl.bsoft.apidemo.synchroniseren.service.UpdateCounter;
import nl.bsoft.apidemo.synchroniseren.util.TaskSemaphore;
import nl.bsoft.bestuurlijkegrenzen.generated.model.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nullable;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SynchronizeController {
    private BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService;
    private OpenbareLichamenProcessingService openbareLichamenProcessingService;

    @Autowired
    SynchronizeController(BestuurlijkeGrenzenProcessingService bestuurlijkeGrenzenProcessingService, OpenbareLichamenProcessingService openbareLichamenProcessingService, TaskSemaphore taskSemaphore) {
        this.bestuurlijkeGrenzenProcessingService = bestuurlijkeGrenzenProcessingService;
        this.openbareLichamenProcessingService = openbareLichamenProcessingService;
    }

    @Operation(
            operationId = "bestuurlijkeGebiedenSynchronisation",
            summary = "Synchronize bestuurlijkegebieden",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/bestuurlijkegebieden",
            produces = {"application/json", "application/problem+json"}
    )
    public ResponseEntity<UpdateCounter> startBestuurlijkgebied(@RequestParam("validAt") @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validAt) {

        UpdateCounter counter = new UpdateCounter();

        if (validAt == null) {
            validAt = LocalDate.now();
        }

        counter = bestuurlijkeGrenzenProcessingService.processBestuurlijkeGebieden(validAt);

        return ResponseEntity.ok(counter);
    }

    @Operation(
            operationId = "openbarelichamenynchronisation",
            summary = "Synchronize openbarelichamen",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCounter.class)),
                            @Content(mediaType = "application/problem+json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/openbarelichamen",
            produces = {"application/json", "application/problem+json"}
    )
    public ResponseEntity<UpdateCounter> startOpenbaarLichaam(@RequestParam("validAt") @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validAt) {

        UpdateCounter counter = new UpdateCounter();

        if (validAt == null) {
            validAt = LocalDate.now();
        }

        counter = openbareLichamenProcessingService.processOpenbareLichamen(validAt);

        return ResponseEntity.ok(counter);
    }
}
