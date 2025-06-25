package co.com.bancolombia.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FranchiseRequestDto {
    private String name;
}
