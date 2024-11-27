package sa.m.ntd.calculator.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import sa.m.ntd.calculator.converter.OperationTypeConverter;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "OPERATION", uniqueConstraints = { @UniqueConstraint(columnNames = {"operation_type"} )})
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Convert(converter = OperationTypeConverter.class)
    @Column(name = "operation_type", nullable = false)
    private OperationType type;

    @Column(name = "cost", nullable = false, precision = 8, scale = 2)
    private BigDecimal cost;

    public Operation(OperationType type, BigDecimal cost) {
        this.type = type;
        this.cost = cost;
    }
}
