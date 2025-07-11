package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.r2dbc.entity.ProductEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.repository.ProductR2dbcRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public class ProductR2dbcAdapter extends ReactiveAdapterOperations<
        Product,
        ProductEntity,
        UUID,
        ProductR2dbcRepository
        > implements ProductRepository {

    public ProductR2dbcAdapter(ProductR2dbcRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Product.class));
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        return Mono.just(this.toData(product))
                .flatMap(entity -> {
                    entity.setIsNew(Boolean.TRUE);
                    return this.repository.save(entity);
                })
                .map(this::toEntity);
    }

    @Override
    public Mono<Product> updateProduct(Product product) {
        return Mono.just(this.toData(product))
                .flatMap(entity -> {
                    entity.setIsNew(Boolean.FALSE);
                    return this.repository.save(entity);
                })
                .map(this::toEntity);
    }

    @Override
    public Mono<Product> getProductByName(String name, UUID branchId) {
        return Mono.defer(() -> {
                    Product model = new Product();
                    model.setName(name);
                    model.setBranchId(branchId);
                    return Mono.just(model);
                })
                .flatMap(productModel -> this.findByExample(productModel)
                        .collectList()
                        .filter(list -> !list.isEmpty())
                        .map(List::getFirst)
                );
    }

    @Override
    public Mono<Product> getProductById(UUID id) {
        return this.findById(id);
    }

    @Override
    public Mono<Void> removeProduct(Product product) {
        return this.repository.delete(this.toData(product));
    }

    @Override
    public Flux<Product> getTopProductsForBranchAndFranchise(UUID franchise) {
        return this.repository.getTopProductByBranchToFranchise(franchise)
                .map(productTop -> Product.builder()
                                    .id(productTop.getProductid())
                                    .name(productTop.getProductname())
                                    .stock(productTop.getProductstock())
                                    .branch(Branch.builder()
                                            .id(productTop.getBranchid())
                                            .name(productTop.getBranchname())
                                            .build()
                                    )
                                    .build()
                );
    }
}
