package nl.bsoft.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import java.io.Serializable;

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

    @Column(name="identificatie", length = 48)
    private String identificatie;

    @Column(name="domein", length = 48)
    private String domein;

    @Column(name="gebiedtype", length = 32)
    private String type;

    @Column(name="geometrie", nullable = false, columnDefinition = "geometry(Geometry,28992)")
    private Geometry geometrie;

}
