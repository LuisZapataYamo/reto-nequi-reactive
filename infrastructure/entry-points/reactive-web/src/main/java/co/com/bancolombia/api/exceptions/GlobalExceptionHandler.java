package co.com.bancolombia.api.exceptions;

import co.com.bancolombia.api.exceptions.dto.ErrorResponseDto;
import co.com.bancolombia.model.enums.IExceptionMessage;
import co.com.bancolombia.model.exception.ApiException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties.Resources resources,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        super.setMessageReaders(codecConfigurer.getReaders());
        super.setMessageWriters(codecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        int code = status.value();

        if (error instanceof ApiException apiEx) {
            IExceptionMessage exceptionMessage;
            exceptionMessage = apiEx.getExceptionMessage();
            code = Integer.parseInt(exceptionMessage.getHttpCode());
            status = HttpStatus.valueOf(code);
        }

        ErrorResponseDto responseBody = ErrorResponseDto.builder()
                .message(error.getMessage())
                .code(code)
                .path(request.path())
                .timestamp(LocalDateTime.now().toString())
                .build();

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseBody);
    }
}
