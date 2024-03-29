package nl.bsoft.apidemo.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "locatie", schema = "public", catalog = "ambtsgebied")
public class LocatieDto implements Serializable {
    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "md5hash", nullable = false)
    private String md5hash;

    @Column(name = "geometrie", nullable = false, columnDefinition = "geometry(Geometry,28992)")
    private Geometry geometrie;

    @Column(name = "registratie")
    private LocalDateTime registratie;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocatieDto that = (LocatieDto) o;
        return Objects.equals(md5hash, that.md5hash) && Objects.equals(geometrie, that.geometrie) && Objects.equals(registratie, that.registratie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(md5hash, geometrie, registratie);
    }
}
