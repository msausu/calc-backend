package sa.m.ntd.calculator.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sa.m.ntd.calculator.dto.AuthRequest;

@RestController
@CrossOrigin(
        origins = { "http://localhost", "http://127.0.0.1" },
        allowCredentials = "true",
        allowPrivateNetwork = "true",
        allowedHeaders = {"Authorization", "Origin"},
        exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Authorization"},
        methods = {RequestMethod.OPTIONS, RequestMethod.POST},
        maxAge = 3600L
)
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/login-form", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> formLogin(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated())
                return ResponseEntity.ok().headers(bearerAuthorizarionHeader(authentication.getName())).body("");
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    public HttpHeaders bearerAuthorizarionHeader(String username) {
        String token = jwtUtil.generateToken(username);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
