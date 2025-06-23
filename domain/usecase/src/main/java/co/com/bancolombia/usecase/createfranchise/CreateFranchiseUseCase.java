package co.com.bancolombia.usecase.createfranchise;

import co.com.bancolombia.model.enums.FranchiseExceptionMessage;
import co.com.bancolombia.model.exception.FranchiseException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class CreateFranchiseUseCase {
    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> createFranchise(Franchise franchise) {
        return franchiseRepository.getFranchiseByName(franchise.getName())
                .switchIfEmpty(Mono.defer(() -> {
                    franchise.setId(UUID.randomUUID());
                    return franchiseRepository.saveFranchise(franchise);
                }))
                .flatMap(existing -> Mono.error(new FranchiseException(FranchiseExceptionMessage.FRANCHISE_WITH_NAME_EXIST)));
    }
}
