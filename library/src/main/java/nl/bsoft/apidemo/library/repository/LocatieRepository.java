package nl.bsoft.apidemo.library.repository;

import nl.bsoft.apidemo.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.apidemo.library.model.dto.LocatieDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocatieRepository extends PagingAndSortingRepository<LocatieDto, Long>,
        CrudRepository<LocatieDto, Long>,
        JpaSpecificationExecutor<LocatieDto> {

    @Query(
            value =
                    "SELECT * FROM locatie WHERE md5hash = :md5hash", nativeQuery = true)
    Optional<LocatieDto> findByMd5hash(@Param("md5hash") String md5hash);

}

