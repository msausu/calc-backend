package sa.m.ntd.calculator.data;

import sa.m.ntd.calculator.model.Operation;
import static sa.m.ntd.calculator.model.OperationType.*;

import java.math.BigDecimal;
import java.util.List;

public interface OperationData {

    public static final List<Operation> OPERATION_LIST = List.of(
            new Operation(ADDITION, BigDecimal.valueOf(0.1)),
            new Operation(SUBTRACTION, BigDecimal.valueOf(0.1)),
            new Operation(MULTIPLICATION, BigDecimal.valueOf(0.2)),
            new Operation(DIVISION, BigDecimal.valueOf(0.5)),
            new Operation(SQUARE_ROOT, BigDecimal.valueOf(0.75)),
            new Operation(RANDOM_STRING, BigDecimal.valueOf(1.5))
    );

    public static final String INVALID_OPERATION = "Invalid operation";
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance";
    public static final String EXTERNAL_RANDOM_ENDPOINT = "https://www.random.org/strings/?num=1&len=8&digits=on&unique=on&format=plain&rnd=new";
}
