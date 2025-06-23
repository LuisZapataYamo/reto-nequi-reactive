package co.com.bancolombia.usecase.removeproductfrombranch;

import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.enums.ProductExceptionMessage;
import co.com.bancolombia.model.exception.ProductException;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class RemoveProductFromBranchUseCase {
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<Boolean> removeProductFromBranch(String branchId, String productId) {
        return  Mono.defer(() -> {
                    try{
                        UUID branchUUID = UUID.fromString(branchId);
                        return branchRepository.getBranchById(branchUUID)
                                .switchIfEmpty(Mono.error(new ProductException(ProductExceptionMessage.BRANCH_NOT_EXISTS, branchId)));
                    }catch(IllegalArgumentException e){
                        return Mono.error(new ProductException(ProductExceptionMessage.BRANCHID_NOT_VALID, branchId));
                    }
                })
                .flatMap(unused -> {
                    try{
                        UUID productUUID = UUID.fromString(productId);
                        return productRepository.getProductById(productUUID)
                                .switchIfEmpty(Mono.error(new ProductException(ProductExceptionMessage.PRODUCT_NOT_EXISTS, productId)));
                    }catch(IllegalArgumentException e){
                        return Mono.error(new ProductException(ProductExceptionMessage.PRODUCTID_NOT_VALID, productId));
                    }
                })
                .flatMap(productRepository::removeProduct)
                .thenReturn(Boolean.TRUE);
    }
}
