package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.FranchiseHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFranchiseRest {

    @Bean(name = "franchisesRouterRestBean")
    public RouterFunction<ServerResponse> routerFranchiseFunction(FranchiseHandler handler) {
        return route(POST("/api/franchises"), handler::createFranchise)
                    .andRoute(GET("/api/franchises/{franchiseId}/products/top"), handler::getFranchise);
    }
}
