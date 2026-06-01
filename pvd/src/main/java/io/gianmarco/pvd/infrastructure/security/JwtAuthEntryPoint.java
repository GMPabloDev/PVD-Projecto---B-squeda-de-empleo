package io.gianmarco.pvd.infrastructure.security;

import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final JsonMapper mapper = JsonMapper.builder().build();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var message = authException.getMessage() != null
                ? authException.getMessage()
                : "Authentication required";

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 401,
                "error", "UNAUTHORIZED",
                "message", message,
                "path", request.getRequestURI());

        mapper.writeValue(response.getOutputStream(), body);
    }
}