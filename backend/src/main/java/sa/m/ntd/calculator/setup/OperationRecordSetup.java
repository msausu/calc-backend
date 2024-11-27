package sa.m.ntd.calculator.setup;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sa.m.ntd.calculator.model.CalculatorUser;
import sa.m.ntd.calculator.model.Operation;
import sa.m.ntd.calculator.model.OperationRecord;
import sa.m.ntd.calculator.model.OperationType;
import sa.m.ntd.calculator.repo.CalculatorUserRepository;
import sa.m.ntd.calculator.repo.OperationRecordRepository;
import sa.m.ntd.calculator.repo.OperationRepository;

import java.math.BigDecimal;

@Component
@Order(3)
class OperationRecordSetup implements ApplicationRunner {

    private final OperationRecordRepository operationRecordRepository;
    private final OperationRepository operationRepository;
    private final CalculatorUserRepository calculatorUserRepository;

    public OperationRecordSetup(OperationRecordRepository operationRecordRepository,
                                OperationRepository operationRepository,
                                CalculatorUserRepository calculatorUserRepository) {
        this.operationRecordRepository = operationRecordRepository;
        this.operationRepository = operationRepository;
        this.calculatorUserRepository = calculatorUserRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CalculatorUser user = calculatorUserRepository.getReferenceById("john@gmail.com");
        Operation operation = operationRepository.findByType(OperationType.RANDOM_STRING);
        operationRecordRepository.save(OperationRecord.builder()
                .user(user)
                .operation(operation)
                .userBalance(BigDecimal.valueOf(100))
                .amount("")
                .operationResponse("NA")
                .build());
        user = calculatorUserRepository.getReferenceById("mary@gmail.com");
        operationRecordRepository.save(OperationRecord.builder()
                .user(user)
                .operation(operation)
                .userBalance(BigDecimal.valueOf(100))
                .amount("")
                .operationResponse("NA")
                .build());
    }
}