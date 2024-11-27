package sa.m.ntd.calculator.repo;

/**
 *
 * @author msa
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sa.m.ntd.calculator.model.OperationRecord;
import sa.m.ntd.calculator.model.OperationType;

import java.util.List;

@PreAuthorize("hasRole('ROLE_USER')")
public interface OperationRecordQueryRepository extends PagingAndSortingRepository<OperationRecord, String> {

    @Query("SELECT o FROM OperationRecord o WHERE o.user.username = :username and o.isExcluded = false")
    public Page<OperationRecordProjection> findByUsername(String username, Pageable pageable);

    @Query("""
            SELECT o
                FROM OperationRecord o
            WHERE
                o.user.username = :username
                and o.operation.type in :type
                and o.isExcluded = false
            """)
    public Page<OperationRecordProjection> findByUsernameAndOperation(String username, List<OperationType> type, Pageable pageable);

}