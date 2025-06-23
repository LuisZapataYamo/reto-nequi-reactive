package co.com.bancolombia.model.franchise.gateways;

import co.com.bancolombia.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> saveFranchise(Franchise franchise);
    Mono<Franchise> getFranchiseByName(String name);
}
