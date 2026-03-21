package com.socialseed.authservice.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.socialseed.apiresponse.model.ApiResponse;
import com.socialseed.errorhandling.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        
        Throwable cause = authException.getCause();
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = "error.internal";

        if (cause instanceof BusinessException be) {
            status = be.getErrorCode().getHttpStatus().value();
            try {
                message = ApiResponse.msg(be.getErrorCode().getCode(), be.getParams());
            } catch (Exception e) {
                message = be.getErrorCode().getCode();
            }
        } else if (cause != null) {
            message = cause.getMessage();
        } else {
            message = authException.getMessage();
        }

        writeError(response, status, message);
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        if (!response.isCommitted()) {
            response.resetBuffer();
            response.setStatus(status);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            var body = new ApiResponse<Void>(status, null, message, "v0.0.1", Instant.now());
            response.getWriter().write(mapper.writeValueAsString(body));
            response.getWriter().flush();
        }
    }
}
