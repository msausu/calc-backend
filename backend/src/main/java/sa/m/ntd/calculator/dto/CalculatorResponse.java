package sa.m.ntd.calculator.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CalculatorResponse {

    private BigDecimal balance;
    private String operationResponse;
}
