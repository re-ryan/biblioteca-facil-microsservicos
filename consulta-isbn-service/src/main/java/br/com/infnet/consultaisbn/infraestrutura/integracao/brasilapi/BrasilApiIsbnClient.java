package br.com.infnet.consultaisbn.infraestrutura.integracao.brasilapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "brasilApiIsbnClient", url = "${integracao.brasilapi.url}")
public interface BrasilApiIsbnClient {

    @GetMapping("/api/isbn/v1/{isbn}")
    BrasilApiLivroResponseDto consultar(final @PathVariable String isbn);
}
