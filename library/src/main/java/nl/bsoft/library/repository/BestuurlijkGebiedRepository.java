package nl.bsoft.library.repository;

import nl.bsoft.library.model.dto.BestuurlijkGebiedDto;
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

    List<BestuurlijkGebiedDto> findBestuurlijkGebiedDtoByIdentificatie(@Param("identificatie") String identificatie);

    @Query(
            value =
                    "SELECT * FROM bestuurlijkgebied WHERE begingeldigheid <= :peilmoment AND ((eindgeldigheid IS NULL) OR (eindgeldigheid > :peilmoment))", nativeQuery = true)
    List<BestuurlijkGebiedDto> findBestuurlijkGebiedActueel(Pageable pageable, LocalDate peilmoment);

    @Query(
            value =
                    "SELECT * FROM bestuurlijkgebied WHERE (identificatie = :identificatie ) AND ((begingeldigheid <= :peilmoment) AND ((eindgeldigheid IS NULL) OR (eindgeldigheid > :peilmoment))) ", nativeQuery = true)
    List<BestuurlijkGebiedDto> findBestuurlijkGebiedDtoByIdentificatieActueel(@Param("identificatie") String identificatie, @Param("peilmoment") LocalDate peilmoment);
}
