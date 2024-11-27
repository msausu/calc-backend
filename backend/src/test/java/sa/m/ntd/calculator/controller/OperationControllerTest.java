package sa.m.ntd.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sa.m.ntd.calculator.dto.CalculatorRequest;
import sa.m.ntd.calculator.dto.CalculatorResponse;
import sa.m.ntd.calculator.model.OperationType;
import sa.m.ntd.calculator.service.OperationService;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OperationController.class)
@Import(TestSecurityConfig.class)
class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperationService service;

    @Autowired
    private ObjectMapper objectMapper;

    private CalculatorRequest calculatorRequest;
    private CalculatorResponse calculatorResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calculatorRequest = CalculatorRequest.builder()
                .operation(OperationType.ADDITION)
                .amount("10 20")
                .build();
        calculatorResponse = CalculatorResponse.builder()
                .operationResponse("30")
                .balance(BigDecimal.valueOf(0))
                .build();
    }

    @Test
    void testCalculate() throws Exception {
        when(service.calculate(calculatorRequest)).thenReturn(calculatorResponse);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/calculator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(calculatorRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.operationResponse").value("30"));
    }

}
