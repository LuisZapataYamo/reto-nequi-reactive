package co.com.bancolombia.usecase.modifyproduct;

import co.com.bancolombia.model.enums.ProductExceptionMessage;
import co.com.bancolombia.model.exception.ProductException;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class ModifyProductUseCase {
    private final ProductRepository productRepository;

    public Mono<Product> modifyProduct(String productId, Product product) {
        return Mono.fromCallable(() -> UUID.fromString(productId))
                .onErrorResume(e -> Mono.error(new ProductException(ProductExceptionMessage.PRODUCTID_NOT_VALID, productId)))
                .flatMap(productUUID -> productRepository.getProductById(productUUID)
                        .switchIfEmpty(Mono.error(new ProductException(ProductExceptionMessage.PRODUCT_NOT_EXISTS, productId))))
                .flatMap(savedProduct -> {
                    boolean isStockPresent = product.getStock() != null;
                    boolean isNamePresent = product.getName() != null;

                    if (!isStockPresent && !isNamePresent) {
                        return Mono.error(new ProductException(ProductExceptionMessage.UPDATE_PRODUCT_STOCK_NAME_NULL));
                    }

                    boolean isStockChanged = isStockPresent && !Objects.equals(product.getStock(), savedProduct.getStock());
                    boolean isNameChanged = isNamePresent && !Objects.equals(product.getName(), savedProduct.getName());

                    if (!isStockChanged && !isNameChanged) {
                        return Mono.error(new ProductException(ProductExceptionMessage.UPDATE_PRODUCT_STOCK_NAME_NOT_CHANGES));
                    }

                    if (isStockChanged) {
                        savedProduct.setStock(product.getStock());
                    }

                    if (isNameChanged) {
                        savedProduct.setName(product.getName());
                        return productRepository.getProductByName(savedProduct.getName(), savedProduct.getBranchId())
                                .flatMap(unused -> Mono.<Product>error(new ProductException(ProductExceptionMessage.PRODUCT_WITH_NAME_EXIST)))
                                .switchIfEmpty(productRepository.updateProduct(savedProduct));
                    }

                    return productRepository.updateProduct(savedProduct);
                });
    }
}
