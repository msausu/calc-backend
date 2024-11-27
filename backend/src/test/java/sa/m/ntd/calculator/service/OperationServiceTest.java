package sa.m.ntd.calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sa.m.ntd.calculator.model.OperationType;
import sa.m.ntd.calculator.repo.CalculatorUserRepository;
import sa.m.ntd.calculator.repo.OperationRecordRepository;
import sa.m.ntd.calculator.repo.OperationRepository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static sa.m.ntd.calculator.data.OperationData.INVALID_OPERATION;

@Slf4j
class OperationServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private CalculatorUserRepository calculatorUserRepository;

    @Mock
    private OperationRecordRepository operationRecordRepository;

    @Mock
    private RandomStringService randomStringService;

    @InjectMocks
    private OperationService operationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAdditionWithValidArgs() {
        assertEquals("5", operationService.calculate(OperationType.ADDITION, Optional.of(List.of(new BigDecimal("2"), new BigDecimal("3")))));
    }

    @Test
    void testAdditionWithInvalidArgs() {
        assertEquals(INVALID_OPERATION, operationService.calculate(OperationType.ADDITION, Optional.of(List.of(new BigDecimal("2")))));
    }

    @Test
    void testSubtractionWithValidArgs() {
        assertEquals("1", operationService.calculate(OperationType.SUBTRACTION, Optional.of(List.of(new BigDecimal("3"), new BigDecimal("2")))));
    }

    @Test
    void testSubtractionWithInvalidArgs() {
        assertEquals(INVALID_OPERATION, operationService.calculate(OperationType.SUBTRACTION, Optional.of(List.of(new BigDecimal("2")))));
    }

    @Test
    void testMultiplicationWithValidArgs() {
        assertEquals("6", operationService.calculate(OperationType.MULTIPLICATION, Optional.of(List.of(new BigDecimal("2"), new BigDecimal("3")))));
    }

    @Test
    void testMultiplicationWithInvalidArgs() {
        assertEquals(INVALID_OPERATION, operationService.calculate(OperationType.MULTIPLICATION, Optional.of(List.of(new BigDecimal("2")))));
    }

    @Test
    void testDivisionWithValidArgs() {
        assertEquals("2", operationService.calculate(OperationType.DIVISION, Optional.of(List.of(new BigDecimal("6"), new BigDecimal("3")))));
    }

    @Test
    void testDivisionByZero() {
        assertEquals(INVALID_OPERATION, operationService.calculate(OperationType.DIVISION, Optional.of(List.of(new BigDecimal("6"), BigDecimal.ZERO))));
    }

    @Test
    void testDivisionWithInvalidArgs() {
        assertEquals(INVALID_OPERATION, operationService.calculate(OperationType.DIVISION, Optional.of(List.of(new BigDecimal("6")))));
    }

    @Test
    void testSquareRootWithValidArgs() {
        assertEquals("2", operationService.calculate(OperationType.SQUARE_ROOT, Optional.of(List.of(new BigDecimal("4")))));
    }

    @Test
    void testSquareRootWithNegativeArg() {
        assertEquals(INVALID_OPERATION, operationService.calculate(OperationType.SQUARE_ROOT, Optional.of(List.of(new BigDecimal("-4")))));
    }

    @Test
    void testSquareRootWithInvalidArgs() {
        assertEquals(INVALID_OPERATION, operationService.calculate(OperationType.SQUARE_ROOT, Optional.of(List.of(new BigDecimal("4"), new BigDecimal("2")))));
    }

    @Test
    void testRandomStringWithEmptyArgs() {
        when(randomStringService.getNumber()).thenReturn("123456");
        assertEquals("123456", operationService.calculate(OperationType.RANDOM_STRING, Optional.empty()));
    }

    @Test
    void testRandomStringWithNonEmptyArgs() {
        assertEquals(INVALID_OPERATION, operationService.calculate(OperationType.RANDOM_STRING, Optional.of(List.of(new BigDecimal("1")))));
    }

    @Test
    void testExtractArgumentsWithNullInput() {
        Optional<List<BigDecimal>> result = operationService.extractArguments(null);
        assertTrue(result.isEmpty(), "Expected Optional.empty() for null input");
    }

    @Test
    void testExtractArgumentsWithSingleValidNumber() {
        Optional<List<BigDecimal>> result = operationService.extractArguments("123.45");
        assertTrue(result.isPresent());
        assertEquals(List.of(new BigDecimal("123.45", MathContext.DECIMAL64)), result.get());
    }

    @Test
    void testExtractArgumentsWithMultipleValidNumbers() {
        Optional<List<BigDecimal>> result = operationService.extractArguments("123.45 67.89");
        assertTrue(result.isPresent());
        assertEquals(List.of(new BigDecimal("123.45", MathContext.DECIMAL64), new BigDecimal("67.89", MathContext.DECIMAL64)), result.get());
    }

    @Test
    void testExtractArgumentsWithInvalidAndValidNumbers() {
        Optional<List<BigDecimal>> result = operationService.extractArguments("123.45 abc 67.89");
        assertTrue(result.isPresent());
        assertEquals(List.of(new BigDecimal("123.45", MathContext.DECIMAL64), new BigDecimal("67.89", MathContext.DECIMAL64)), result.get());
    }

    @Test
    void testExtractArgumentsWithWhitespace() {
        Optional<List<BigDecimal>> result = operationService.extractArguments("   123.45   67.89   ");
        assertTrue(result.isPresent());
        assertEquals(List.of(new BigDecimal("123.45", MathContext.DECIMAL64), new BigDecimal("67.89", MathContext.DECIMAL64)), result.get());
    }

    @Test
    void testExtractArgumentsWithEmptyString() {
        Optional<List<BigDecimal>> result = operationService.extractArguments("");
        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty(), "Expected an empty list for empty string input");
    }

    @Test
    void testExtractArgumentsWithOnlyInvalidNumbers() {
        Optional<List<BigDecimal>> result = operationService.extractArguments("abc def");
        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty(), "Expected an empty list for input with only invalid numbers");
    }
}
