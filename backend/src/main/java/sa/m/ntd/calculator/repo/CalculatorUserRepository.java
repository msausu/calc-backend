package sa.m.ntd.calculator.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sa.m.ntd.calculator.model.CalculatorUser;

@Repository
public interface CalculatorUserRepository extends JpaRepository<CalculatorUser, String> {
}
