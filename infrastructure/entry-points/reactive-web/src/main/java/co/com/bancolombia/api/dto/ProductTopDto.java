package co.com.bancolombia.api.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductTopDto {
    private UUID id;
    private String name;
    private Integer stock;
    private BranchSimpleDto branch;
}
