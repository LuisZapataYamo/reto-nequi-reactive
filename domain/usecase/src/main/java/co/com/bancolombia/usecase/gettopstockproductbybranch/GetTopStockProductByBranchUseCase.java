package co.com.bancolombia.usecase.gettopstockproductbybranch;

import co.com.bancolombia.model.enums.FranchiseExceptionMessage;
import co.com.bancolombia.model.exception.FranchiseException;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class GetTopStockProductByBranchUseCase {
    private final ProductRepository productRepository;

    public Flux<Product> getTopStockProductByBranch(String franchiseId){
        return Mono.fromCallable(() -> UUID.fromString(franchiseId))
                .onErrorResume(error -> Mono.error(new FranchiseException(FranchiseExceptionMessage.FRANCHISEID_NOT_VALID)))
                .flatMapMany(productRepository::getTopProductsForBranchAndFranchise);
    }
}
