package co.com.bancolombia.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "branch")
public class BranchEntity {
    @Id
    private Integer id;
    private String name;

    @Column("franchise_id")
    private Integer franchiseId;

    @Transient
    private List<ProductEntity> products;
}
