package co.com.bancolombia.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "franchise")
public class FranchiseEntity implements Persistable<UUID> {
    @Id
    private UUID id;
    private String name;

    @Transient
    private List<BranchEntity> branches;

    @Transient
    private Boolean isNew;

    @Override
    public boolean isNew() {
        return this.isNew;
    }
}
