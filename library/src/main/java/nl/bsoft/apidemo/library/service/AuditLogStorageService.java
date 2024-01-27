package nl.bsoft.apidemo.library.service;

import lombok.extern.slf4j.Slf4j;
import nl.bsoft.apidemo.library.model.dto.AuditLogDto;
import nl.bsoft.apidemo.library.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuditLogStorageService {
    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditLogStorageService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLogDto Save(AuditLogDto auditLogDto) {
        log.debug("Saving {}", auditLogDto.toString());
        AuditLogDto savedauditLogDto = auditLogRepository.save(auditLogDto);

        return savedauditLogDto;
    }

}
