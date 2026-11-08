package br.com.infnet.bibliotecafacil.catalogo.aplicacao.service;

import br.com.infnet.bibliotecafacil.biblioteca.dominio.Acervo;
import br.com.infnet.bibliotecafacil.biblioteca.infraestrutura.repository.AcervoRepository;
import br.com.infnet.bibliotecafacil.catalogo.aplicacao.dto.LivroCatalogo;
import br.com.infnet.bibliotecafacil.catalogo.aplicacao.dto.LivroCatalogo.DisponibilidadeBiblioteca;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Autoria;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Categoria;
import br.com.infnet.bibliotecafacil.catalogo.dominio.Livro;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CatalogoService {

    private final AcervoRepository acervoRepository;

    public CatalogoService(final AcervoRepository acervoRepository) {
        this.acervoRepository = acervoRepository;
    }

    public List<LivroCatalogo> buscar(
            final String titulo,
            final String autor,
            final String categoria) {
        final List<Acervo> acervos = this.acervoRepository.buscarNoCatalogo(
                this.normalizarFiltro(titulo),
                this.normalizarFiltro(autor),
                this.normalizarFiltro(categoria));

        final Map<Long, List<Acervo>> acervosPorLivro = acervos.stream()
                .sorted(Comparator
                        .comparing((Acervo acervo) -> acervo.getLivro().getTitulo(),
                                String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(acervo -> acervo.getBiblioteca().getNome(),
                                String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.groupingBy(
                        acervo -> acervo.getLivro().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()));

        return acervosPorLivro.values().stream()
                .map(this::criarItemCatalogo)
                .toList();
    }

    private LivroCatalogo criarItemCatalogo(final List<Acervo> acervos) {
        final Livro livro = acervos.getFirst().getLivro();
        final List<String> autores = livro.getAutorias().stream()
                .map(Autoria::getAutor)
                .filter(autor -> autor.isAtivo())
                .map(autor -> autor.getNome())
                .distinct()
                .toList();
        final List<String> categorias = livro.getCategorias().stream()
                .filter(Categoria::isAtiva)
                .map(Categoria::getNome)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
        final List<DisponibilidadeBiblioteca> bibliotecas = acervos.stream()
                .map(acervo -> new DisponibilidadeBiblioteca(
                        acervo.getBiblioteca().getId(),
                        acervo.getBiblioteca().getNome(),
                        acervo.getQuantidadeDisponivel()))
                .toList();

        return new LivroCatalogo(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn13(),
                livro.getEditora(),
                livro.getAnoPublicacao(),
                livro.getUrlImagemCapa(),
                autores,
                categorias,
                bibliotecas);
    }

    private String normalizarFiltro(final String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return "";
        }
        return filtro.trim();
    }
}
