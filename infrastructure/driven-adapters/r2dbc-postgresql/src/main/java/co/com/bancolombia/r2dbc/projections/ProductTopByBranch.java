package co.com.bancolombia.r2dbc.projections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductTopByBranch {
    private UUID productid;
    private String productname;
    private Integer productstock;
    private UUID branchid;
    private String branchname;
}
