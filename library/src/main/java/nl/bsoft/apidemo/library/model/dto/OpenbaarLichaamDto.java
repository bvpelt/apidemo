package nl.bsoft.apidemo.library.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "openbaarlichaam", schema = "public", catalog = "ambtsgebied")
public class OpenbaarLichaamDto implements Serializable {
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 48, nullable = false)
    private String code;

    @Column(name = "oin", length = 24)
    private String oin;

    @Column(name = "lichaamtype", length = 32, nullable = false)
    private String type;

    @Column(name = "naam", length = 64)
    private String naam;

    @Column(name = "bestuurslaag", length = 32)
    private String bestuurslaag;

    @Column(name = "beginregistratie")
    private LocalDateTime beginRegistratie;

    @Column(name = "eindregistratie")
    private LocalDateTime eindRegistratie;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenbaarLichaamDto that = (OpenbaarLichaamDto) o;
        return code.equals(that.code) && Objects.equals(oin, that.oin) && type.equals(that.type) && Objects.equals(naam, that.naam) && Objects.equals(bestuurslaag, that.bestuurslaag) && beginRegistratie.equals(that.beginRegistratie) && Objects.equals(eindRegistratie, that.eindRegistratie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, oin, type, naam, bestuurslaag, beginRegistratie, eindRegistratie);
    }

    @Override
    public String toString() {
        return "OpenbaarLichaamDto{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", oin='" + oin + '\'' +
                ", type='" + type + '\'' +
                ", naam='" + naam + '\'' +
                ", bestuurslaag='" + bestuurslaag + '\'' +
                ", beginRegistratie=" + beginRegistratie +
                ", eindRegistratie=" + eindRegistratie +
                '}';
    }
}
