package co.com.bancolombia.model.branch.gateways;

import co.com.bancolombia.model.branch.Branch;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BranchRepository {
    Mono<Branch> saveBranch(Branch branch);
    Mono<Branch> getBranchByName(String name, UUID franchiseId);
    Mono<Branch> getBranchById(UUID id);
}
