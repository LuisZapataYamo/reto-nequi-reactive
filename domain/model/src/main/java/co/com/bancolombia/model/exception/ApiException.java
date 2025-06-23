package co.com.bancolombia.model.exception;


import co.com.bancolombia.model.enums.IExceptionMessage;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final transient IExceptionMessage exceptionMessage;

    public ApiException(IExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
        this.exceptionMessage = exceptionMessage;
    }
}
