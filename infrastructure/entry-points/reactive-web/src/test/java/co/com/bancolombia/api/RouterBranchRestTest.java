package co.com.bancolombia.api;

import co.com.bancolombia.api.config.WebPropertiesConfig;
import co.com.bancolombia.api.dto.BranchRequestDto;
import co.com.bancolombia.api.exceptions.GlobalExceptionHandler;
import co.com.bancolombia.api.handler.BranchHandler;
import co.com.bancolombia.api.router.RouterBranchRest;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.createbranch.CreateBranchUseCase;
import co.com.bancolombia.usecase.removeproductfrombranch.RemoveProductFromBranchUseCase;
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
import reactor.core.publisher.Mono;

import java.util.UUID;

@ContextConfiguration(classes = {
        WebPropertiesConfig.class,
        GlobalExceptionHandler.class,
        ObjectMapper.class,
        RouterBranchRest.class,
        BranchHandler.class,
        CreateBranchUseCase.class,
        RemoveProductFromBranchUseCase.class
})
@WebFluxTest
class RouterBranchRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BranchRepository branchRepository;

    @MockitoBean
    private FranchiseRepository franchiseRepository;

    @MockitoBean
    private ProductRepository productRepository;

    private UUID franchiseId;
    private Branch branch;

    @BeforeEach
    void setup() {
        franchiseId = UUID.randomUUID();
        branch = Branch.builder()
                .id(UUID.randomUUID())
                .name("Branch 1")
                .franchiseId(franchiseId)
                .build();
    }

    @Test
    void testCreateBranch() {
        BranchRequestDto request = BranchRequestDto.builder()
                .name(branch.getName())
                .franchiseId(franchiseId)
                .build();

        Mockito.when(franchiseRepository.getFranchiseById(franchiseId))
                .thenReturn(Mono.just(Franchise.builder().id(franchiseId).name("Franchise").build()));
        Mockito.when(branchRepository.getBranchByName(branch.getName(), franchiseId))
                .thenReturn(Mono.empty());
        Mockito.when(branchRepository.saveBranch(Mockito.any(Branch.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        webTestClient.post()
                .uri("/api/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo(branch.getName());
    }

    @Test
    void testRemoveProductFromBranch() {
        UUID productId = UUID.randomUUID();

        Mockito.when(branchRepository.getBranchById(branch.getId()))
                .thenReturn(Mono.just(branch));
        Mockito.when(productRepository.getProductById(productId))
                .thenReturn(Mono.just(Product.builder().id(productId).build()));
        Mockito.when(productRepository.removeProduct(Mockito.any(Product.class)))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/branches/{branchId}/products/{productId}", branch.getId(), productId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testRemoveProductFromBranch_invalidBranchId() {
        webTestClient.delete()
                .uri("/api/branches/{branchId}/products/{productId}", "invalid", UUID.randomUUID())
                .exchange()
                .expectStatus().isEqualTo(404); // Por GlobalExceptionHandler
    }

    @Test
    void testRemoveProductFromBranch_branchNotFound() {
        UUID productId = UUID.randomUUID();
        UUID invalidBranchId = UUID.randomUUID();

        Mockito.when(branchRepository.getBranchById(invalidBranchId))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/branches/{branchId}/products/{productId}", invalidBranchId, productId)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @Test
    void testRemoveProductFromBranch_productNotFound() {
        UUID productId = UUID.randomUUID();

        Mockito.when(branchRepository.getBranchById(branch.getId()))
                .thenReturn(Mono.just(branch));
        Mockito.when(productRepository.getProductById(productId))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/branches/{branchId}/products/{productId}", branch.getId(), productId)
                .exchange()
                .expectStatus().isEqualTo(404);
    }
}
