package nl.bsoft.library.repository;

import nl.bsoft.library.model.dto.BestuurlijkGebiedDto;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BestuurlijkGebiedRepository extends PagingAndSortingRepository<BestuurlijkGebiedDto, Long>,
        CrudRepository<BestuurlijkGebiedDto, Long>,
        JpaSpecificationExecutor<BestuurlijkGebiedDto> {
}
