package nl.bsoft.apidemo.library.repository;

import nl.bsoft.apidemo.library.model.dto.BestuurlijkGebiedDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BestuurlijkGebiedRepository extends PagingAndSortingRepository<BestuurlijkGebiedDto, Long>,
        CrudRepository<BestuurlijkGebiedDto, Long>,
        JpaSpecificationExecutor<BestuurlijkGebiedDto> {


    @Query(
            value =
                    "SELECT * FROM bestuurlijkgebied WHERE identificatie = :identificatie order by beginregistratie desc, begingeldigheid desc", nativeQuery = true)
    List<BestuurlijkGebiedDto> findBestuurlijkGebiedDtoByIdentificatie(@Param("identificatie") String identificatie);

    @Query(
            value =
                    "SELECT * FROM bestuurlijkgebied WHERE begingeldigheid <= :validAt AND ((eindgeldigheid IS NULL) OR (eindgeldigheid > :validAt))", nativeQuery = true)
    List<BestuurlijkGebiedDto> findBestuurlijkGebiedActueel(Pageable pageable, LocalDate validAt);

    @Query(
            value =
                    "SELECT * FROM bestuurlijkgebied WHERE (identificatie = :identificatie ) AND ((begingeldigheid <= :validAt) AND ((eindgeldigheid IS NULL) OR (eindgeldigheid > :validAt))) ", nativeQuery = true)
    List<BestuurlijkGebiedDto> findBestuurlijkGebiedDtoByIdentificatieActueel(@Param("identificatie") String identificatie, @Param("validAt") LocalDate validAt);
}

