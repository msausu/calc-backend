package sa.m.ntd.calculator.converter;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sa.m.ntd.calculator.model.OperationType;

/**
 *
 * @author msa
 */
@Component
@ConfigurationPropertiesBinding
class OperationParamConverter implements Converter<String, OperationType> {

    @Override
    public OperationType convert(String operationType) {
        return OperationType.valueOf(operationType);
    }
}
