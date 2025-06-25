package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.r2dbc.entity.FranchiseEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.repository.FranchiseR2dbcRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public class FranchiseR2dbcAdapter extends ReactiveAdapterOperations<
        Franchise,
        FranchiseEntity,
        UUID,
        FranchiseR2dbcRepository
        > implements FranchiseRepository {

    public FranchiseR2dbcAdapter(FranchiseR2dbcRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Franchise.class));
    }

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        return Mono.just(this.toData(franchise))
                .flatMap(fraEntity -> {
                    fraEntity.setIsNew(Boolean.TRUE);
                    return this.repository.save(fraEntity);
                })
                .map(this::toEntity);
    }

    @Override
    public Mono<Franchise> getFranchiseByName(String name) {
        return Mono.defer(() -> {
                    Franchise model = new Franchise();
                    model.setName(name);
                    return Mono.just(model);
                })
                .flatMap(franchiseModel -> this.findByExample(franchiseModel)
                        .collectList()
                        .filter(list -> !list.isEmpty())
                        .map(List::getFirst)
                );
    }

    @Override
    public Mono<Franchise> getFranchiseById(UUID id) {
        return this.findById(id);
    }
}
