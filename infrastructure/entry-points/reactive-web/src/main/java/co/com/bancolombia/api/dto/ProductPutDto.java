package co.com.bancolombia.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductPutDto {
    private String name;
    private Integer stock;
}
