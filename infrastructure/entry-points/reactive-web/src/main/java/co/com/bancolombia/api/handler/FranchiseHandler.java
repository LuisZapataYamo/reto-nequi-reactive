package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.FranchiseRequestDto;
import co.com.bancolombia.api.dto.ProductTopDto;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.createfranchise.CreateFranchiseUseCase;
import co.com.bancolombia.usecase.gettopstockproductbybranch.GetTopStockProductByBranchUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {
    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final GetTopStockProductByBranchUseCase getTopStockProductByBranchUseCase;
    private final ObjectMapper objectMapper;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseRequestDto.class)
                .map(body -> objectMapper.convertValue(body, Franchise.class))
                .flatMap(createFranchiseUseCase::createFranchise)
                .flatMap(response -> ServerResponse
                        .created(URI.create("/franchise/" + response.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> getFranchise(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");

        Flux<ProductTopDto> dtoFlux = getTopStockProductByBranchUseCase
                .getTopStockProductByBranch(franchiseId)
                .map(e -> objectMapper.convertValue(e, ProductTopDto.class));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dtoFlux, ProductTopDto.class);
    }

}
