package co.com.bancolombia.usecase.createbranch;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.enums.BranchExceptionMessage;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class CreateBranchUseCase {
    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;

    public Mono<Branch> createBranch(Branch branch) {
        return franchiseRepository.getFranchiseById(branch.getFranchiseId())
                .switchIfEmpty(Mono.error(new BranchException(BranchExceptionMessage.FRANCHISE_NOT_EXISTS, branch.getFranchiseId().toString())))
                .flatMap(unused -> branchRepository.getBranchByName(branch.getName()))
                .flatMap(unused -> Mono.<Branch>error(new BranchException(BranchExceptionMessage.BRANCH_WITH_NAME_EXIST)))
                .switchIfEmpty(Mono.defer(() -> {
                    branch.setId(UUID.randomUUID());
                    return branchRepository.saveBranch(branch);
                }));
    }
}
