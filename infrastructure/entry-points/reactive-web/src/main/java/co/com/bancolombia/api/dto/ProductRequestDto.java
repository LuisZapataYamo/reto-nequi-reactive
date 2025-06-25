package co.com.bancolombia.api.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductRequestDto {
    private String name;
    private Integer stock;
    private UUID branchId;
}
