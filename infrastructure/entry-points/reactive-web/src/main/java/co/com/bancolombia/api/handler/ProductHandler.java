package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.ProductRequestDto;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.usecase.createproduct.CreateProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class ProductHandler {
    private final CreateProductUseCase createProductUseCase;
    private final ObjectMapper objectMapper;

    public Mono<ServerResponse> createProduct(ServerRequest request) {
        return request.bodyToMono(ProductRequestDto.class)
                .map(body -> objectMapper.convertValue(body, Product.class))
                .flatMap(createProductUseCase::createProduct)
                .flatMap(response -> ServerResponse
                        .created(URI.create("/products/" + response.getId()))
                        .bodyValue(response));
    }

}
