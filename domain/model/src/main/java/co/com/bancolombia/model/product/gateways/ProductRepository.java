package co.com.bancolombia.model.product.gateways;

import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> saveProduct(Product product);
    Mono<Product> getProductByName(String name);
}
