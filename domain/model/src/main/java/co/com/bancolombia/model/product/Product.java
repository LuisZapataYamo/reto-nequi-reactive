package co.com.bancolombia.model.product;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {
    private UUID id;
    private String name;
    private Integer stock;
    private Integer branchId;
}
