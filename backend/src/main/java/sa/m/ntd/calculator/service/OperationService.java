package sa.m.ntd.calculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sa.m.ntd.calculator.dto.CalculatorResponse;
import sa.m.ntd.calculator.dto.CalculatorRequest;
import sa.m.ntd.calculator.model.CalculatorUser;
import sa.m.ntd.calculator.model.Operation;
import sa.m.ntd.calculator.model.OperationRecord;
import sa.m.ntd.calculator.model.OperationType;
import sa.m.ntd.calculator.repo.CalculatorUserRepository;
import sa.m.ntd.calculator.repo.OperationRecordRepository;
import sa.m.ntd.calculator.repo.OperationRepository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static sa.m.ntd.calculator.data.OperationData.*;

@Service
public class OperationService {

    private final OperationRepository operationRepository;
    private final CalculatorUserRepository calculatorUserRepository;
    private final OperationRecordRepository operationRecordRepository;
    private final RandomStringService randomStringService;

    @Autowired
    public OperationService(OperationRepository operationRepository,
                            CalculatorUserRepository calculatorUserRepository,
                            OperationRecordRepository operationRecordRepository,
                            RandomStringService randomStringService) {
        this.operationRepository = operationRepository;
        this.calculatorUserRepository = calculatorUserRepository;
        this.operationRecordRepository = operationRecordRepository;
        this.randomStringService = randomStringService;
    }

    public void setup() {
        if (operationRepository.count() == 0) operationRepository.saveAll(OPERATION_LIST);
    }

    @Transactional
    public CalculatorResponse calculate(CalculatorRequest request) {
        Optional<List<BigDecimal>> args = extractArguments(request.getAmount());
        String result = calculate(request.getOperation(), args);
        Optional<BigDecimal> balance = updateBalance(request.getOperation(), request.getAmount() == null ? "" : request.getAmount(), result);
        return balance.isPresent() ?
                CalculatorResponse.builder().balance(balance.get()).operationResponse(result).build() :
                CalculatorResponse.builder().balance(null).operationResponse(INSUFFICIENT_BALANCE).build();
    }

    Optional<List<BigDecimal>> extractArguments(String amount) {
        if (amount == null) return Optional.empty();
        String[] values = amount.trim().split("\\s+");
        List<BigDecimal> numbers = new ArrayList<>();
        for (String value : values) {
            try {
                numbers.add(new BigDecimal(value, MathContext.DECIMAL64));
            } catch(Exception ignored) {
            }
        }
        return Optional.of(numbers);
    }

    String calculate(OperationType operation, Optional<List<BigDecimal>> args) {
        Predicate<Optional<List<BigDecimal>>> check1 = x -> x.isPresent() && x.get().size() == 1;
        Predicate<Optional<List<BigDecimal>>> check2 = x -> x.isPresent() && x.get().size() == 2;
        return switch (operation) {
            case ADDITION -> check2.test(args) ? args.get().get(0).add(args.get().get(1)).toString() : INVALID_OPERATION;
            case SUBTRACTION -> check2.test(args) ? args.get().get(0).subtract(args.get().get(1)).toString() : INVALID_OPERATION;
            case DIVISION -> check2.test(args) && args.get().get(1).compareTo(BigDecimal.ZERO) != 0 ? args.get().get(0).divide(args.get().get(1), MathContext.DECIMAL64).toString() : INVALID_OPERATION;
            case MULTIPLICATION -> check2.test(args) ? args.get().get(0).multiply(args.get().get(1), MathContext.DECIMAL64).toString() : INVALID_OPERATION;
            case SQUARE_ROOT -> check1.test(args) && args.get().get(0).compareTo(BigDecimal.ZERO) >= 0 ? args.get().get(0).sqrt(MathContext.DECIMAL64).toString() : INVALID_OPERATION;
            case RANDOM_STRING -> args.isEmpty() ? randomStringService.getNumber().replaceAll("\n", "").trim() : INVALID_OPERATION;
            default -> INVALID_OPERATION;
        };
    }
    
    public  Optional<BigDecimal> updateBalance(OperationType operationType, String amount, String result) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CalculatorUser user = calculatorUserRepository.getReferenceById(username);
        Operation operation = operationRepository.findByType(operationType);
        BigDecimal lastBalance = operationRecordRepository.findLastBalanceByUser(username);
        if (lastBalance == null || lastBalance.compareTo(operation.getCost()) < 0) return Optional.empty();
        BigDecimal nextBalance = !result.equals(INVALID_OPERATION) ? lastBalance.subtract(operation.getCost()) : lastBalance;
        OperationRecord newRecord = OperationRecord.builder()
                .isExcluded(false)
                .amount(amount)
                .operation(operation)
                .userBalance(nextBalance)
                .operationResponse(result)
                .user(user)
                .build();
        operationRecordRepository.save(newRecord);
        return Optional.of(nextBalance);
    }
}