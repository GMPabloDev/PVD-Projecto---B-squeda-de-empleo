package io.gianmarco.pvd.infrastructure.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.json.JsonMapper;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final JsonMapper mapper = JsonMapper.builder().build();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 403,
                "error", "FORBIDDEN",
                "message", accessDeniedException.getMessage(),
                "path", request.getRequestURI());

        mapper.writeValue(response.getOutputStream(), body);
    }
}