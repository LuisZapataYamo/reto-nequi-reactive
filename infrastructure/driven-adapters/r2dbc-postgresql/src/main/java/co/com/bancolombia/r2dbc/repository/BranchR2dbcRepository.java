package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.BranchEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface BranchR2dbcRepository extends R2dbcRepository<BranchEntity, UUID> {
}
