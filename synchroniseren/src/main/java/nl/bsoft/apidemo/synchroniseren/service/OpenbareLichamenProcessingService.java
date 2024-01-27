package nl.bsoft.apidemo.synchroniseren.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenbareLichamenProcessingService {

    private OpenbareLichamenImportService openbareLichamenImportService;

    @Autowired
    public OpenbareLichamenProcessingService(OpenbareLichamenImportService openbareLichamenImportService) {
        this.openbareLichamenImportService = openbareLichamenImportService;
    }

    @Async
    public UpdateCounter processOpenbareLichamen(LocalDate validAt) {
        log.debug("Process all openbare lichamen");

        return openbareLichamenImportService.getAllOpenbareLichamen(validAt);
    }
}
