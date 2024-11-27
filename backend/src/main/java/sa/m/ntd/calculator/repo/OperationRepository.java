package sa.m.ntd.calculator.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sa.m.ntd.calculator.model.Operation;
import sa.m.ntd.calculator.model.OperationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Integer> {

    public long count();

    public Operation findByType(OperationType type);
}
