package com.example.springcloudgateway.handler;

import com.example.springcloudgateway.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@Order(-1)
@RequiredArgsConstructor
public class ExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }
        return response.writeWith(Mono.fromSupplier(() -> {
                DataBufferFactory bufferFactory = response.bufferFactory();
                // Error Response 객체 생성해 Json으로 반환
                try {
                    ErrorResponse errorResponse = ErrorResponse.defaultError(ex.getMessage());
                    byte[] byteErrorResponse = objectMapper.writeValueAsBytes(errorResponse);
                    return bufferFactory.wrap(byteErrorResponse);
                } catch (Exception e) {
                    log.error("error", e);
                    return bufferFactory.wrap(new byte[0]);
                }
            }
        ));
    }
}
