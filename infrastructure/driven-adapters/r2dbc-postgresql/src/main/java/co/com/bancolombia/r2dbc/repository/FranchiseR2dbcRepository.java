package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.r2dbc.entity.FranchiseEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FranchiseR2dbcRepository extends R2dbcRepository<FranchiseEntity, UUID> {
    Mono<Franchise> findByName(String name);
}
