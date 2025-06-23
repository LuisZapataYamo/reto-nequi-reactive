package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterProductRest {

    @Bean(name = "productsRouterRestBean")
    public RouterFunction<ServerResponse> routerProductFunction(ProductHandler handler) {
        return route(POST("/api/products"), handler::createProduct);
    }
}
