package co.com.bancolombia.api;

import co.com.bancolombia.api.config.WebPropertiesConfig;
import co.com.bancolombia.api.dto.ProductPutDto;
import co.com.bancolombia.api.dto.ProductRequestDto;
import co.com.bancolombia.api.exceptions.GlobalExceptionHandler;
import co.com.bancolombia.api.handler.ProductHandler;
import co.com.bancolombia.api.router.RouterProductRest;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.createproduct.CreateProductUseCase;
import co.com.bancolombia.usecase.modifyproduct.ModifyProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        RouterProductRest.class,
        ProductHandler.class,
        CreateProductUseCase.class,
        ModifyProductUseCase.class
})
@WebFluxTest
class RouterProductRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private BranchRepository branchRepository;

    @Test
    void testCreateProduct() {
        UUID branchId = UUID.randomUUID();
        ProductRequestDto request = ProductRequestDto.builder()
                .name("New Product")
                .stock(10)
                .branchId(branchId)
                .build();

        Mockito.when(branchRepository.getBranchById(branchId))
                .thenReturn(Mono.just(Branch.builder().id(branchId).name("Branch").build()));
        Mockito.when(productRepository.getProductByName(request.getName(), branchId))
                .thenReturn(Mono.empty());
        Mockito.when(productRepository.saveProduct(Mockito.any(Product.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location")
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("New Product");
    }

    @Test
    void testCreateProduct_branchNotFound() {
        UUID branchId = UUID.randomUUID();
        ProductRequestDto request = ProductRequestDto.builder()
                .name("Product")
                .stock(10)
                .branchId(branchId)
                .build();

        Mockito.when(branchRepository.getBranchById(branchId))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @Test
    void testModifyProduct_success() {
        UUID productId = UUID.randomUUID();
        Product saved = Product.builder()
                .id(productId)
                .name("Old Name")
                .stock(10)
                .branch(Branch.builder().id(UUID.randomUUID()).build())
                .build();

        ProductPutDto dto = ProductPutDto.builder()
                .name("New Name")
                .stock(15)
                .build();

        Mockito.when(productRepository.getProductById(productId))
                .thenReturn(Mono.just(saved));
        Mockito.when(productRepository.getProductByName("New Name", saved.getBranchId()))
                .thenReturn(Mono.empty());
        Mockito.when(productRepository.updateProduct(Mockito.any(Product.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        webTestClient.put()
                .uri("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("New Name")
                .jsonPath("$.stock").isEqualTo(15);
    }

    @Test
    void testModifyProduct_invalidUUID() {
        ProductPutDto dto = ProductPutDto.builder().stock(20).build();

        webTestClient.put()
                .uri("/api/products/{productId}", "invalid-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @Test
    void testModifyProduct_productNotFound() {
        UUID productId = UUID.randomUUID();
        ProductPutDto dto = ProductPutDto.builder().name("Name").build();

        Mockito.when(productRepository.getProductById(productId))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @Test
    void testModifyProduct_noChangesProvided() {
        UUID productId = UUID.randomUUID();
        ProductPutDto dto = ProductPutDto.builder().build();

        Product existing = Product.builder()
                .id(productId)
                .name("Existing")
                .stock(10)
                .branch(Branch.builder().id(UUID.randomUUID()).build())
                .build();

        Mockito.when(productRepository.getProductById(productId))
                .thenReturn(Mono.just(existing));

        webTestClient.put()
                .uri("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(400);
    }

    @Test
    void testModifyProduct_nameAlreadyExists() {
        UUID productId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        ProductPutDto dto = ProductPutDto.builder()
                .name("Duplicate Name")
                .build();

        Product current = Product.builder()
                .id(productId)
                .name("Old")
                .stock(5)
                .branch(Branch.builder().id(branchId).build())
                .build();

        Product sameName = Product.builder()
                .id(UUID.randomUUID())
                .name("Duplicate Name")
                .stock(7)
                .branch(Branch.builder().id(branchId).build())
                .build();

        Mockito.when(productRepository.getProductById(productId)).thenReturn(Mono.just(current));
        Mockito.when(productRepository.getProductByName(dto.getName(), current.getBranchId())).thenReturn(Mono.just(sameName));
        Mockito.when(productRepository.updateProduct(Mockito.any())).thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(409);
    }
}
