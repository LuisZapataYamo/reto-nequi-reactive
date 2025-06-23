package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.r2dbc.entity.BranchEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.repository.BranchR2dbcRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class BranchR2dbcAdapter extends ReactiveAdapterOperations<
        Branch,
        BranchEntity,
        UUID,
        BranchR2dbcRepository
        > implements BranchRepository {

    public BranchR2dbcAdapter(BranchR2dbcRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Branch.class));
    }

    @Override
    public Mono<Branch> saveBranch(Branch branch) {
        return Mono.just(this.toData(branch))
                .flatMap(fraEntity -> {
                    fraEntity.setIsNew(Boolean.TRUE);
                    return this.repository.save(fraEntity);
                })
                .map(this::toEntity);
    }
}
