package sa.m.ntd.calculator.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import sa.m.ntd.calculator.model.OperationType;
import sa.m.ntd.calculator.repo.OperationRecordProjection;
import sa.m.ntd.calculator.repo.OperationRecordQueryRepository;
import sa.m.ntd.calculator.repo.OperationRecordRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.*;

@Slf4j
@RestController
@RequestMapping("api/v1/report")
@CrossOrigin(
        origins = { "http://localhost", "http://127.0.0.1" },
        allowCredentials = "true",
        allowPrivateNetwork = "true",
        allowedHeaders = {"Authorization", "Origin"},
        exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"},
        methods = {RequestMethod.OPTIONS, RequestMethod.POST},
        maxAge = 3600L
)
public class ReportController {

    private final OperationRecordQueryRepository queryService;
    private final OperationRecordRepository service;

    public ReportController(OperationRecordQueryRepository queryService,OperationRecordRepository service) {
        this.queryService = queryService;
        this.service = service;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Page<OperationRecordProjection> report(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "1000", required = false) int pageSize,
            @RequestParam(value = "sortField", defaultValue = "date", required = false) String sortField,
            @RequestParam(value = "sortOrder", defaultValue = "desc", required = false) String sortOrder,
            @RequestParam(value = "filter", defaultValue = "", required = false) String filter
    ) {
        String username = getUsername(request);
        log.info(username);
        Sort.Direction dir = sortOrder != null && sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        if (filter == null || filter.isEmpty()) {
            return queryService.findByUsername(username, PageRequest.of(pageNumber, pageSize).withSort(dir, sortField));
        } else {
            List<OperationType> types = OperationType.find(filter);
            return queryService.findByUsernameAndOperation(username, types, PageRequest.of(pageNumber, pageSize).withSort(dir, sortField));
        }
    }

    @PutMapping(path = "/{id}/is-excluded")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> status(@PathVariable(name = "id", required = true) UUID id, @RequestBody(required = true) boolean status) {
        service.updateOperationRecordStatus(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(produces = TEXT_PLAIN_VALUE, path = "/{user}/last-balance")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String lastBalance(@PathVariable(name = "user", required = true) String user) {
        BigDecimal balance = service.findLastBalanceByUser(user);
        return balance.toString();
    }

    String getUsername(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring("Bearer ".length());
        }
        return new JwtUtil().extractUsername(token);
    }
}
