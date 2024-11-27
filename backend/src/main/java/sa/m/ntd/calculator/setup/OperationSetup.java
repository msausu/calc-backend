package sa.m.ntd.calculator.setup;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sa.m.ntd.calculator.service.OperationService;

@Component
@Order(1)
class OperationSetup implements ApplicationRunner {

    private OperationService operationService;

    public OperationSetup(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        operationService.setup();
    }
}