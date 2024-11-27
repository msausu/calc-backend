package sa.m.ntd.calculator.repo;

/**
 *
 * @author msa
 */

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sa.m.ntd.calculator.model.OperationRecord;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface OperationRecordRepository extends JpaRepository<OperationRecord, String> {

    @Query("""
        SELECT
                o.userBalance
            FROM OperationRecord o
            WHERE
                o.user.username = :user
                AND o.date = (SELECT MAX(o2.date) FROM OperationRecord o2 WHERE o2.user.username = :user)
        """)
    public BigDecimal findLastBalanceByUser(@Param("user") String user);

    @Transactional
    @Modifying
    @Query(value = "UPDATE OperationRecord o set o.isExcluded = :status where o.id = :id")
    public void updateOperationRecordStatus(@Param("id") UUID id, @Param("status") boolean status);

}