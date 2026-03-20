package com.socialseed.errorhandling.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.socialseed.apiresponse.model.ApiResponse;
import com.socialseed.errorhandling.handler.GlobalErrorHandler;
import com.socialseed.errorhandling.handler.PGSQLExceptionHandler;
import com.socialseed.errorhandling.handler.Neo4jExceptionHandler;
import com.socialseed.errorhandling.handler.RedisExceptionHandler;
import com.socialseed.errorhandling.handler.KafkaExceptionHandler;
import com.socialseed.errorhandling.handler.SecurityExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@AutoConfiguration
public class ErrorHandlingAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandlingAutoConfiguration.class);
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Bean
    GlobalErrorHandler globalErrorHandler() {
        return new GlobalErrorHandler();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.dao.DataIntegrityViolationException")
    PGSQLExceptionHandler pgsqlExceptionHandler() {
        return new PGSQLExceptionHandler();
    }

    @Bean
    @ConditionalOnClass(name = "org.neo4j.driver.exceptions.ClientException")
    Neo4jExceptionHandler neo4jExceptionHandler() {
        return new Neo4jExceptionHandler();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.data.redis.RedisConnectionFactory")
    RedisExceptionHandler redisExceptionHandler() {
        return new RedisExceptionHandler();
    }

    @Bean
    @ConditionalOnClass(name = "org.apache.kafka.clients.producer.ProducerRecord")
    KafkaExceptionHandler kafkaExceptionHandler() {
        return new KafkaExceptionHandler();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.security.access.AccessDeniedException")
    SecurityExceptionHandler securityExceptionHandler() {
        return new SecurityExceptionHandler();
    }

    @Bean
    PlatformErrorController platformErrorController(ErrorAttributes errorAttributes) {
        return new PlatformErrorController(errorAttributes);
    }

    public static class PlatformErrorController implements ErrorController {
        private final ErrorAttributes errorAttributes;

        public PlatformErrorController(ErrorAttributes errorAttributes) {
            this.errorAttributes = errorAttributes;
        }

        @RequestMapping("/error")
        public void handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
            if (response.isCommitted()) {
                return;
            }

            int status = response.getStatus();
            Map<String, Object> attrs = errorAttributes.getErrorAttributes(
                    new ServletWebRequest(request),
                    org.springframework.boot.web.error.ErrorAttributeOptions.defaults()
            );
            String message = (String) attrs.getOrDefault("message", "Unknown error");

            response.setStatus(status);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            var body = new ApiResponse<Void>(status, null, message, "v0.0.1", Instant.now());
            response.getWriter().write(mapper.writeValueAsString(body));
            response.getWriter().flush();
        }
    }
}
