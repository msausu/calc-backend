package sa.m.ntd.calculator.controller;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sa.m.ntd.calculator.dto.CalculatorResponse;
import sa.m.ntd.calculator.dto.CalculatorRequest;
import sa.m.ntd.calculator.service.OperationService;

@Slf4j
@RestController
@RequestMapping("api/v1/calculator")
@CrossOrigin(
        origins = { "http://localhost", "http://127.0.0.1" },
        allowCredentials = "true",
        allowPrivateNetwork = "true",
        allowedHeaders = {"Authorization", "Origin"},
        exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"},
        methods = {RequestMethod.OPTIONS, RequestMethod.POST},
        maxAge = 3600L
)
public class OperationController {

    private final OperationService service;

    public OperationController(OperationService service) {
        this.service = service;
    }

    @CrossOrigin
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public CalculatorResponse calculate(@NotNull @RequestBody CalculatorRequest request) {
        log.info("req {}", request);
        return service.calculate(request);
    }

}
