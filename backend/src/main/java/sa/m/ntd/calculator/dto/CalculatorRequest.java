package sa.m.ntd.calculator.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import sa.m.ntd.calculator.model.OperationType;

@Builder
@Data
public class CalculatorRequest {

    private String amount;

    @NotNull
    private OperationType operation;

}
