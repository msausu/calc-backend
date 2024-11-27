package sa.m.ntd.calculator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null, jwt = null;
        String requestPath = request.getRequestURI();

        if (requestPath.equals("/login-form")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (authorizationHeader == null) {
                throw new io.jsonwebtoken.security.SignatureException("is missing");
            }
            if (authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring("Bearer ".length()).trim();
                username = jwtUtil.extractUsername(jwt);
            }
            if (username != null && Boolean.TRUE.equals(jwtUtil.validateToken(jwt, username))) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            chain.doFilter(request, response);
        }  catch (io.jsonwebtoken.security.SignatureException ex) {
            log.error("*** Invalid token {}", ex.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
