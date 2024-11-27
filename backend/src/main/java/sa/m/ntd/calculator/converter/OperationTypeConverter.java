package sa.m.ntd.calculator.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import sa.m.ntd.calculator.model.OperationType;

/**
 *
 * @author msa
 */
@Converter
public class OperationTypeConverter implements AttributeConverter<OperationType, String> {

    @Override
    public String convertToDatabaseColumn(OperationType operationType) {
        return operationType.name();
    }

    @Override
    public OperationType convertToEntityAttribute(String operationType) {
        return OperationType.valueOf(operationType);
    }
}
