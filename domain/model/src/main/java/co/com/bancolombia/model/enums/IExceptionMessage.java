package co.com.bancolombia.model.enums;

import java.io.Serializable;

public interface IExceptionMessage extends Serializable {
    String getMessage();
    String getHttpCode();
}
