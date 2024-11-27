package sa.m.ntd.calculator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "RECORD", uniqueConstraints = { @UniqueConstraint(columnNames = {"id"}) })
public class OperationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "operation_id", referencedColumnName = "id")
    private Operation operation;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_username", referencedColumnName = "username")
    private CalculatorUser user;
    @Column(name = "amount", nullable = false)
    private String amount;
    @Column(name = "user_balance", nullable = false)
    private BigDecimal userBalance;
    @Column(name = "operational_response", nullable = false)
    private String operationResponse;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:SS", locale = "en-US", timezone = "GMT/UTC")
    @PastOrPresent
    @CreationTimestamp(source = SourceType.DB)
    private Instant date;
    @ColumnDefault("false")
    private boolean isExcluded;
}
