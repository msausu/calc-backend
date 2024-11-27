package sa.m.ntd.calculator.repo;

import sa.m.ntd.calculator.model.OperationType;

import java.math.BigDecimal;
import java.time.Instant;
public interface OperationRecordProjection {

  public String getId();
  public Instant getDate();
  public OperationSummary getOperation();
  public String getAmount();
  public String getOperationResponse();
  public BigDecimal getUserBalance();

  interface OperationSummary {
    OperationType getType();
  }

}