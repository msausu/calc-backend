package sa.m.ntd.calculator.setup;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sa.m.ntd.calculator.model.CalculatorUser;
import sa.m.ntd.calculator.repo.CalculatorUserRepository;

import java.util.List;

@Component
@Order(2)
class CalculatorUserSetup implements ApplicationRunner {

    private final CalculatorUserRepository calculatorUserRepository;
    private final List<CalculatorUser> users;

    public CalculatorUserSetup(CalculatorUserRepository calculatorUserRepository, List<CalculatorUser> users) {
        this.calculatorUserRepository = calculatorUserRepository;
        this.users = users;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        calculatorUserRepository.saveAll(users);
    }
}