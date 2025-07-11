package co.com.bancolombia.api.exceptions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorResponseDto {
    private String message;
    private int code;
    private String path;
    private String timestamp;
}
