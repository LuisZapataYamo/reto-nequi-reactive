package co.com.bancolombia.api.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BranchSimpleDto {
    private UUID id;
    private String name;
}
