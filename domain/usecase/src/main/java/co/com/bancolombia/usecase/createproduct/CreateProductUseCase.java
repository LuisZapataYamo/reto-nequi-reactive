package co.com.bancolombia.usecase.createproduct;

import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.enums.ProductExceptionMessage;
import co.com.bancolombia.model.exception.ProductException;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class CreateProductUseCase {
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<Product> createProduct(Product product) {
        return branchRepository.getBranchById(product.getBranchId())
                .switchIfEmpty(Mono.error(new ProductException(ProductExceptionMessage.BRANCH_NOT_EXISTS, product.getBranchId().toString())))
                .flatMap(unused -> productRepository.getProductByName(product.getName(), product.getBranchId()))
                .flatMap(unused -> Mono.<Product>error(new ProductException(ProductExceptionMessage.PRODUCT_WITH_NAME_EXIST)))
                .switchIfEmpty(Mono.defer(() -> {
                    product.setId(UUID.randomUUID());
                    return productRepository.saveProduct(product);
                }));
    }
}
