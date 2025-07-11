package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FranchiseSimpleResponseDto {
    private Integer id;
    private String name;
}
