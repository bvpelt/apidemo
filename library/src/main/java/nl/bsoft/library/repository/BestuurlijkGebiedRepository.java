package nl.bsoft.library.repository;

import nl.bsoft.library.model.dto.BestuurlijkGebiedDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BestuurlijkGebiedRepository extends PagingAndSortingRepository<BestuurlijkGebiedDto, Long>,
        CrudRepository<BestuurlijkGebiedDto, Long>,
        JpaSpecificationExecutor<BestuurlijkGebiedDto> {

    String FIND_ALL_BESTUURLIJKGEBIED_BY_IDENTIFICATIE_QUERY = "SELECT b " +
            "FROM BestuurlijkgebiedDto b " +
            "WHERE b.identificatie = :identificatie ";


    List<BestuurlijkGebiedDto> findBestuurlijkGebiedDtoByIdentificatie(@Param("identificatie") String identificatie);
}
