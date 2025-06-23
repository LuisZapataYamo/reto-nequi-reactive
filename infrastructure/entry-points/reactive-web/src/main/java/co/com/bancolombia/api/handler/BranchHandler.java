package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.BranchRequestDto;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.usecase.createbranch.CreateBranchUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class BranchHandler {
    private final CreateBranchUseCase createBranchUseCase;
    private final ObjectMapper objectMapper;

    public Mono<ServerResponse> createBranch(ServerRequest request) {
        return request.bodyToMono(BranchRequestDto.class)
                .map(body -> objectMapper.convertValue(body, Branch.class))
                .flatMap(createBranchUseCase::createBranch)
                .flatMap(response -> ServerResponse
                        .created(URI.create("/branches/" + response.getId()))
                        .bodyValue(response));
    }

}
