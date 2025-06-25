package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.BranchHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterBranchRest {

    @Bean(name = "branchesRouterRestBean")
    public RouterFunction<ServerResponse> routerBranchFunction(BranchHandler handler) {
        return route(POST("/api/branches"), handler::createBranch)
                .andRoute(DELETE("/api/branches/{branchId}/products/{productId}"), handler::removeProductFromBranch);
    }
}
