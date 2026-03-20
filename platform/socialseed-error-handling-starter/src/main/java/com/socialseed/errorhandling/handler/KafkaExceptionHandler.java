package com.socialseed.errorhandling.handler;

import com.socialseed.apiresponse.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KafkaExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(KafkaExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleKafka(Exception ex) throws Exception {
        String className = ex.getClass().getName();
        if (className.contains("Kafka") || className.contains("ProducerFailed")
                || className.contains("RecordTooLarge") || className.contains("Serialization")) {
            log.error("Kafka error: {} - {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            String message = ApiResponse.msg("error.kafka.generic");
            int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
            if (className.contains("RecordTooLarge")) {
                message = ApiResponse.msg("error.kafka.record_too_large");
                status = HttpStatus.PAYLOAD_TOO_LARGE.value();
            } else if (className.contains("Serialization")) {
                message = ApiResponse.msg("error.kafka.serialization");
                status = HttpStatus.BAD_REQUEST.value();
            }
            return ResponseEntity.status(status)
                    .body(ApiResponse.error(status, message));
        }
        throw ex;
    }
}
