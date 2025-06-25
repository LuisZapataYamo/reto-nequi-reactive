package co.com.bancolombia.api;

import co.com.bancolombia.api.config.WebPropertiesConfig;
import co.com.bancolombia.api.dto.FranchiseRequestDto;
import co.com.bancolombia.api.exceptions.GlobalExceptionHandler;
import co.com.bancolombia.api.handler.FranchiseHandler;
import co.com.bancolombia.api.router.RouterFranchiseRest;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.createfranchise.CreateFranchiseUseCase;
import co.com.bancolombia.usecase.gettopstockproductbybranch.GetTopStockProductByBranchUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@ContextConfiguration(classes = {WebPropertiesConfig.class, GlobalExceptionHandler.class, ObjectMapper.class, RouterFranchiseRest.class, FranchiseHandler.class, CreateFranchiseUseCase.class, GetTopStockProductByBranchUseCase.class})
@WebFluxTest
class RouterFranchiseRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FranchiseRepository franchiseRepository;

    @MockitoBean
    private ProductRepository productRepository;

    @BeforeEach
    void setUp(){
        Mockito.when(franchiseRepository.saveFranchise(Mockito.any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
    }

    @Test
    void testCreateFranchise() {
        FranchiseRequestDto requestDto = FranchiseRequestDto.builder()
                .name("Test Franchise")
                .build();

        Mockito.when(franchiseRepository.getFranchiseByName(requestDto.getName()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("Test Franchise");
    }

    @Test
    void testCreateFranchise_shouldNameExist() {
        FranchiseRequestDto requestDto = FranchiseRequestDto.builder()
                .name("Franchise")
                .build();

        Franchise franchiseExist = Franchise.builder()
                .name(requestDto.getName())
                        .build();

        Mockito.when(franchiseRepository.getFranchiseByName(requestDto.getName()))
                .thenReturn(Mono.just(franchiseExist));

        webTestClient.post()
                .uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    void testGetTopStockProductByBranch() {
        UUID franchiseId = UUID.randomUUID();
        Product product1 = Product.builder()
                        .id(UUID.randomUUID())
                        .name("Product 1")
                        .stock(10)
                        .branch(Branch.builder()
                                .id(UUID.randomUUID())
                                .name("Branch 1")
                                .build()
                        )
                        .build();
        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 2")
                .stock(10)
                .branch(Branch.builder()
                        .id(UUID.randomUUID())
                        .name("Branch 2")
                        .build()
                )
                .build();

        Mockito.when(productRepository.getTopProductsForBranchAndFranchise(franchiseId)).thenReturn(Flux.just(product1, product2));

        webTestClient.get()
                .uri("/api/franchises/{franchiseId}/products/top", franchiseId.toString())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGetTopStockProductByBranch_shouldErrorFranchiseId() {
        String franchiseId = "aaa";

        webTestClient.get()
                .uri("/api/franchises/{franchiseId}/products/top", franchiseId)
                .exchange()
                .expectStatus().isEqualTo(404);
    }
}



