package nl.bsoft.apidemo.library.repository;

import nl.bsoft.apidemo.library.model.dto.OpenbaarLichaamDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OpenbaarLichaamRepository extends PagingAndSortingRepository<OpenbaarLichaamDto, Long>,
        CrudRepository<OpenbaarLichaamDto, Long>,
        JpaSpecificationExecutor<OpenbaarLichaamDto> {

    @Query(
            value =
                    "SELECT * FROM openbaarlichaam WHERE code = :code order by beginregistratie desc", nativeQuery = true)
    List<OpenbaarLichaamDto> findOpenbaarLichaamDtoByCode(@Param("code") String code);

    @Query(
            value =
                    "SELECT * FROM openbaarlichaam WHERE code = :code AND (beginRegistratie <= :validAt AND ((eindRegistratie > :validAt) OR (eindRegistratie is null)))", nativeQuery = true)
    List<OpenbaarLichaamDto> findOpenbaarLichaamDtoByCodeAtDate(@Param("validAt") LocalDateTime validAt, @Param("code") String code);

    @Query(
            value =
                    "SELECT * FROM openbaarlichaam WHERE (beginRegistratie <= :validAt AND ((eindRegistratie > :validAt) OR (eindRegistratie is null)))",
            countQuery =
                    "SELECT * FROM openbaarlichaam WHERE (beginRegistratie <= :validAt AND ((eindRegistratie > :validAt) OR (eindRegistratie is null)))",
            nativeQuery = true)
    List<OpenbaarLichaamDto> findOpenbaarLichamenDtoByCodeAtDate(@Param("validAt") LocalDateTime validAt, PageRequest page);

}
