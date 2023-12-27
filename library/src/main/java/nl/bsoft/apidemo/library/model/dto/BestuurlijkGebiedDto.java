package nl.bsoft.apidemo.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "bestuurlijkgebied", schema = "public", catalog = "ambtsgebied")
public class BestuurlijkGebiedDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identificatie", length = 48, nullable = false)
    private String identificatie;

    @Column(name = "domein", length = 48)
    private String domein;

    @Column(name = "gebiedtype", length = 32)
    private String type;


    @Column(name = "md5hash", nullable = false)
    private String md5hash;

    @Column(name = "geometrie", nullable = false, columnDefinition = "geometry(Geometry,28992)")
    private Geometry geometrie;

    @Column(name = "begingeldigheid", nullable = false)
    private LocalDate beginGeldigheid;

    @Column(name = "eindgeldigheid")
    private LocalDate eindGeldigheid;

    @Column(name = "beginregistratie")
    private LocalDateTime beginRegistratie;

    @Column(name = "eindregistratie")
    private LocalDateTime eindRegistratie;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BestuurlijkGebiedDto that = (BestuurlijkGebiedDto) o;
        return identificatie.equals(that.identificatie) && Objects.equals(domein, that.domein) && Objects.equals(type, that.type) && md5hash.equals(that.md5hash) && beginGeldigheid.equals(that.beginGeldigheid) && Objects.equals(eindGeldigheid, that.eindGeldigheid) && beginRegistratie.equals(that.beginRegistratie) && Objects.equals(eindRegistratie, that.eindRegistratie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificatie, domein, type, md5hash, beginGeldigheid, eindGeldigheid, beginRegistratie, eindRegistratie);
    }

    @Override
    public String toString() {
        return "BestuurlijkGebiedDto{" +
                "id=" + id +
                ", identificatie='" + identificatie + '\'' +
                ", domein='" + domein + '\'' +
                ", type='" + type + '\'' +
                ", md5hash='" + md5hash + '\'' +
                ", beginGeldigheid=" + beginGeldigheid +
                ", eindGeldigheid=" + eindGeldigheid +
                ", beginRegistratie=" + beginRegistratie +
                ", eindRegistratie=" + eindRegistratie +
                '}';
    }
}
