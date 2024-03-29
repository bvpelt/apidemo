package nl.bsoft.apidemo.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "auditlog", schema = "public", catalog = "ambtsgebied")
public class AuditLogDto {
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jobid", length = 48)
    private String jobid;

    @Column(name = "jobname", length = 48)
    private String jobname;

    @Column(name = "jobstate")
    private String jobstate;

    @Column(name = "validat")
    private LocalDate validat;

    @Column(name = "result")
    private String result;

    @Column(name = "registratie")
    private LocalDateTime registratie;

    @Column(name = "added")
    private int added;

    @Column(name = "updated")
    private int updated;

    @Column(name = "unmodified")
    private int unmodified;

    @Column(name = "removed")
    private int removed;

    @Column(name = "skipped")
    private int skipped;

    @Column(name = "processed")
    private int processed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditLogDto that = (AuditLogDto) o;
        return added == that.added && updated == that.updated && unmodified == that.unmodified && removed == that.removed && skipped == that.skipped && processed == that.processed && jobid.equals(that.jobid) && jobname.equals(that.jobname) && jobstate.equals(that.jobstate) && Objects.equals(validat, that.validat) && Objects.equals(result, that.result) && Objects.equals(registratie, that.registratie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobid, jobname, jobstate, validat, result, registratie, added, updated, unmodified, removed, skipped, processed);
    }

    @Override
    public String toString() {
        return "AuditLogDto{" +
                "id=" + id +
                ", jobid='" + jobid + '\'' +
                ", jobname='" + jobname + '\'' +
                ", jobstate='" + jobstate + '\'' +
                ", validat=" + validat +
                ", result='" + result + '\'' +
                ", registratie=" + registratie +
                ", added=" + added +
                ", updated=" + updated +
                ", unmodified=" + unmodified +
                ", removed=" + removed +
                ", skipped=" + skipped +
                ", processed=" + processed +
                '}';
    }
}
