package nl.bsoft.library.repository;

import nl.bsoft.library.model.dto.OpenbaarLichaamDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenbaarLichaamRepository extends PagingAndSortingRepository<OpenbaarLichaamDto, Long>,
        CrudRepository<OpenbaarLichaamDto, Long>,
        JpaSpecificationExecutor<OpenbaarLichaamDto> {

    String FIND_ALL_OPENBAARLICHAAM_BY_IDENTIFICATIE_QUERY = "SELECT b " +
            "FROM OpenbaarLichaamDto b " +
            "WHERE b.code = :code ";


    List<OpenbaarLichaamDto> findOpenbaarLichaamDtoByCode(@Param("code") String code);
}
