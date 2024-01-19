package nl.bsoft.apidemo.library.repository;

import nl.bsoft.apidemo.library.model.dto.OpenbaarLichaamDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenbaarLichaamRepository extends PagingAndSortingRepository<OpenbaarLichaamDto, Long>,
        CrudRepository<OpenbaarLichaamDto, Long>,
        JpaSpecificationExecutor<OpenbaarLichaamDto> {

    @Query(
            value =
                    "SELECT * FROM openbaarlichaam WHERE code = :code order by beginregistratie desc", nativeQuery = true)
    List<OpenbaarLichaamDto> findOpenbaarLichaamDtoByCode(@Param("code") String code);
}
