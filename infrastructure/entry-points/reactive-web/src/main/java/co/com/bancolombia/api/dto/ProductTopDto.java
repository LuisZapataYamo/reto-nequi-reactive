package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
public class ProductTopDto {
    private UUID id;
    private String name;
    private Integer stock;
    private BranchSimpleDto branch;
}
