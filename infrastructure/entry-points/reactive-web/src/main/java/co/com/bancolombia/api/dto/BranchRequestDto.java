package co.com.bancolombia.api.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BranchRequestDto {
    private String name;
    private UUID franchiseId;
}
