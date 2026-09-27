package br.com.infnet.bibliotecafacil.catalogo.infraestrutura.integracao.isbn;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "consultaIsbnClient", url = "${servicos.consulta-isbn.url}")
public interface ConsultaIsbnClient {

    @GetMapping("/api/isbn/{isbn}")
    MetadadosLivroResponseDto consultar(final @PathVariable String isbn);
}
