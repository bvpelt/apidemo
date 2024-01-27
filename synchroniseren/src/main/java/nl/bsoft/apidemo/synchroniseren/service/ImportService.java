package nl.bsoft.apidemo.synchroniseren.service;

import lombok.RequiredArgsConstructor;
import nl.bsoft.apidemo.library.model.dto.AuditLogDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImportService {
    protected static final int MAX_PAGE_SIZE = 50;

    public AuditLogDto createAuditLog(String jobName, LocalDateTime timestamp, LocalDate validAt, String state) {
        AuditLogDto auditLogDto = new AuditLogDto();

        UUID jobid = UUID.randomUUID();

        auditLogDto.setJobid(jobid.toString());
        auditLogDto.setJobname(jobName);
        auditLogDto.setRegistratie(timestamp);
        auditLogDto.setValidat(validAt);
        auditLogDto.setJobstate(state);

        return auditLogDto;
    }

    public AuditLogDto createAuditLogEnd(AuditLogDto auditLogDto, String state, UpdateCounter counter, String result) {
        AuditLogDto copyAuditLogDto = new AuditLogDto();
        copyAuditLogDto.setJobname(auditLogDto.getJobname());
        copyAuditLogDto.setJobid(auditLogDto.getJobid());
        copyAuditLogDto.setJobstate(state);
        copyAuditLogDto.setValidat(auditLogDto.getValidat());
        copyAuditLogDto.setAdded(counter.getAdded());
        copyAuditLogDto.setRemoved(counter.getRemoved());
        copyAuditLogDto.setProcessed(counter.getProcessed());
        copyAuditLogDto.setSkipped(counter.getSkipped());
        copyAuditLogDto.setUpdated(counter.getUpdated());
        copyAuditLogDto.setUnmodified(counter.getUnmodified());
        copyAuditLogDto.setRegistratie(LocalDateTime.now());
        copyAuditLogDto.setResult(result);

        return copyAuditLogDto;
    }
}
