package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.ProductEntity;
import co.com.bancolombia.r2dbc.projections.ProductTopByBranch;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductR2dbcRepository extends R2dbcRepository<ProductEntity, UUID> {

    @Query("""
        SELECT
          p.id AS productId,
          p.name AS productName,
          p.stock AS productStock,
          p.branch_id AS branchId,
          b.name AS branchName
        FROM product p
        JOIN branch b ON b.id = p.branch_id AND b.franchise_id = :franchiseId
        WHERE (p.branch_id, p.stock) IN (
            SELECT p2.branch_id, MAX(p2.stock)
            FROM product p2
            GROUP BY p2.branch_id
        )
    """)
    Flux<ProductTopByBranch> getTopProductByBranchToFranchise(@Param("franchiseId") UUID franchiseId);
}
