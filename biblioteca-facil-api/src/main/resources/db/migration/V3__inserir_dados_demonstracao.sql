DO $dados_demonstracao$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM public.livro) THEN
        INSERT INTO public.endereco (id, cep, logradouro, numero, complemento, bairro, cidade, uf, latitude, longitude, data_criacao, data_atualizacao) VALUES (1, '20040002', 'Rua do Software Livre', '42', 'Sala 101', 'Centro', 'Rio de Janeiro', 'RJ', NULL, NULL, '2026-11-08 14:46:21.998434', '2026-11-08 14:46:21.998434');
        INSERT INTO public.endereco (id, cep, logradouro, numero, complemento, bairro, cidade, uf, latitude, longitude, data_criacao, data_atualizacao) VALUES (2, '20010020', 'Avenida dos Livros', '100', NULL, 'Centro', 'Rio de Janeiro', 'RJ', NULL, NULL, '2026-11-08 14:46:21.998434', '2026-11-08 14:46:21.998434');
        INSERT INTO public.endereco (id, cep, logradouro, numero, complemento, bairro, cidade, uf, latitude, longitude, data_criacao, data_atualizacao) VALUES (3, '20510060', 'Rua das Palmeiras', '215', NULL, 'Tijuca', 'Rio de Janeiro', 'RJ', NULL, NULL, '2026-11-08 14:46:21.998434', '2026-11-08 14:46:21.998434');
        INSERT INTO public.biblioteca (id, nome, cpf_cnpj, email, telefone, endereco_id, data_criacao, ativa, data_atualizacao) VALUES (1, 'Biblioteca Linux', '11222333000181', 'linux-etapa4@biblioteca.local', '21999990000', 1, '2026-10-03 16:22:38.391904', true, '2026-11-08 14:46:21.998434');
        INSERT INTO public.biblioteca (id, nome, cpf_cnpj, email, telefone, endereco_id, data_criacao, ativa, data_atualizacao) VALUES (2, 'Biblioteca Central', '22333444000155', 'central@biblioteca.local', '(21) 3000-1000', 2, '2026-11-08 14:41:16.339602', true, '2026-11-08 14:46:21.998434');
        INSERT INTO public.biblioteca (id, nome, cpf_cnpj, email, telefone, endereco_id, data_criacao, ativa, data_atualizacao) VALUES (3, 'Biblioteca Comunitária do Bairro', '33444555000166', 'comunitaria@biblioteca.local', '(21) 3000-2000', 3, '2026-11-08 14:41:16.339602', true, '2026-11-08 14:46:21.998434');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (1, 'Domain-Driven Design', NULL, '9780321125217', 'Addison-Wesley', 2003, '1', NULL, NULL, true, '2026-10-03 16:20:54.344152', '2026-10-03 16:20:54.344152');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (2, 'Clean Architecture', NULL, '9780134494166', 'Prentice Hall', 2017, '1', NULL, NULL, true, '2026-10-03 16:20:54.345054', '2026-10-03 16:20:54.345054');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (3, 'Refactoring', NULL, '9780134757599', 'Addison-Wesley', 2018, '2', NULL, NULL, true, '2026-10-03 16:20:54.345198', '2026-10-03 16:20:54.345198');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (4, 'Implementing Domain-Driven Design', NULL, '9780321834577', 'Addison-Wesley', 2013, '1', NULL, NULL, true, '2026-10-03 16:20:54.345294', '2026-10-03 16:20:54.345294');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (5, 'A Hora da Estrela — edição de bolso', NULL, '9788532508126', 'Rocco', NULL, '2', NULL, NULL, false, '2026-11-07 16:47:55.063209', '2026-11-07 16:48:16.135459');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (6, 'O Hobbit', NULL, '9788595084742', 'HarperCollins', 2019, '1', 'Uma aventura pela Terra-média.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (7, '1984', NULL, '9788535914849', 'Companhia das Letras', 2009, '1', 'Um clássico sobre vigilância e autoritarismo.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (8, 'Dom Casmurro', NULL, '9788594318602', 'Principis', 2019, '1', 'Clássico da literatura brasileira.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (9, 'Capitães da Areia', NULL, '9788535914061', 'Companhia das Letras', 2008, '1', 'Romance sobre jovens que vivem nas ruas de Salvador.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (10, 'O Cortiço', NULL, '9788572327953', 'Martin Claret', 2012, '1', 'Romance naturalista da literatura brasileira.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (11, 'O Pequeno Príncipe', NULL, '9788595081512', 'HarperCollins', 2018, '1', 'Uma história sobre amizade, afeto e responsabilidade.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (12, 'Harry Potter e a Pedra Filosofal', NULL, '9788532530783', 'Rocco', 2017, '1', 'O início da jornada de Harry Potter em Hogwarts.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (13, 'Neuromancer', NULL, '9788576573005', 'Aleph', 2016, '5', 'Romance que marcou o gênero cyberpunk.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (14, 'Fundação', NULL, '9788576570660', 'Aleph', 2009, '1', 'O primeiro volume da série Fundação.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (15, 'Sapiens: Uma Breve História da Humanidade', NULL, '9788525432186', 'L&PM', 2015, '1', 'Uma visão ampla da trajetória da humanidade.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (16, 'Código Limpo', NULL, '9788576082675', 'Alta Books', 2009, '1', 'Práticas para escrever código legível e sustentável.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (17, 'O Programador Pragmático', NULL, '9788577807005', 'Bookman', 2010, '1', 'Práticas e reflexões para o desenvolvimento de software.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (18, 'Design Patterns', NULL, '9780201633610', 'Addison-Wesley', 1994, '1', 'Padrões reutilizáveis de projeto de software.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (19, 'Entendendo Algoritmos', NULL, '9788575225639', 'Novatec', 2017, '1', 'Introdução ilustrada a algoritmos e estruturas de dados.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (20, 'Cem Anos de Solidão', NULL, '9788501012074', 'Record', 2019, '1', 'A história da família Buendía e da cidade de Macondo.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro (id, titulo, isbn10, isbn13, editora, ano_publicacao, edicao, descricao, url_imagem_capa, ativo, data_criacao, data_atualizacao) VALUES (21, 'Ensaio sobre a Cegueira', NULL, '9788535911108', 'Companhia das Letras', 2008, '1', 'Uma reflexão sobre a sociedade diante de uma epidemia.', NULL, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (2, 3, 9, 2, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (3, 1, 19, 5, 4, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (4, 3, 7, 1, 0, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (5, 1, 3, 2, 0, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (6, 2, 20, 4, 3, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (7, 3, 3, 3, 2, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (8, 1, 7, 4, 3, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (9, 3, 14, 1, 0, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (10, 3, 19, 3, 2, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (11, 1, 15, 4, 3, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (12, 1, 4, 2, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (13, 2, 6, 5, 4, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (14, 2, 2, 2, 0, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (15, 1, 16, 2, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (16, 2, 18, 2, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (17, 3, 21, 2, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (18, 2, 12, 5, 4, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (19, 1, 21, 3, 2, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (20, 2, 17, 2, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (21, 3, 15, 2, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (22, 2, 11, 6, 5, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (23, 1, 13, 2, 0, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (24, 3, 10, 1, 0, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (25, 2, 13, 2, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (26, 3, 6, 2, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (27, 2, 1, 4, 3, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (28, 3, 11, 3, 2, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (29, 2, 4, 1, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (30, 1, 2, 3, 2, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (31, 1, 10, 2, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (32, 1, 12, 3, 2, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (33, 1, 18, 2, 0, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (34, 3, 17, 2, 1, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (35, 2, 16, 3, 2, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (36, 2, 7, 3, 2, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (37, 2, 9, 3, 2, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (38, 1, 8, 2, 0, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (39, 3, 8, 4, 3, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (40, 2, 14, 3, 2, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (41, 3, 20, 1, 0, true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.acervo (id, biblioteca_id, livro_id, quantidade_real, quantidade_disponivel, ativo, data_criacao, data_atualizacao) VALUES (1, 1, 1, 2, 1, true, '2026-10-03 16:22:51.403476', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (1, 'Eric Evans', 'Evans, Eric', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (2, 'Robert C. Martin', 'Martin, Robert C.', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (3, 'Martin Fowler', 'Fowler, Martin', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (4, 'Vaughn Vernon', 'Vernon, Vaughn', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (5, 'J. R. R. Tolkien', 'Tolkien, J. R. R.', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (6, 'George Orwell', 'Orwell, George', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (7, 'Machado de Assis', 'Assis, Machado de', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (8, 'Jorge Amado', 'Amado, Jorge', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (9, 'Aluísio Azevedo', 'Azevedo, Aluísio', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (10, 'Antoine de Saint-Exupéry', 'Saint-Exupéry, Antoine de', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (11, 'J. K. Rowling', 'Rowling, J. K.', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (12, 'William Gibson', 'Gibson, William', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (13, 'Isaac Asimov', 'Asimov, Isaac', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (14, 'Yuval Noah Harari', 'Harari, Yuval Noah', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (15, 'Andrew Hunt', 'Hunt, Andrew', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (16, 'David Thomas', 'Thomas, David', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (17, 'Erich Gamma', 'Gamma, Erich', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (18, 'Richard Helm', 'Helm, Richard', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (19, 'Ralph Johnson', 'Johnson, Ralph', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (20, 'John Vlissides', 'Vlissides, John', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (21, 'Aditya Bhargava', 'Bhargava, Aditya', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (22, 'Gabriel García Márquez', 'Márquez, Gabriel García', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autor (id, nome, nome_catalogacao, ativo, data_criacao, data_atualizacao) VALUES (23, 'José Saramago', 'Saramago, José', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (1, 18, 19, 3);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (2, 17, 15, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (3, 6, 5, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (4, 3, 3, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (5, 12, 11, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (6, 7, 6, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (7, 2, 2, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (8, 18, 17, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (9, 18, 18, 2);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (10, 4, 4, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (11, 18, 20, 4);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (12, 9, 8, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (13, 1, 1, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (14, 21, 23, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (15, 11, 10, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (16, 15, 14, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (17, 19, 21, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (18, 14, 13, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (19, 17, 16, 2);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (20, 10, 9, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (21, 13, 12, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (22, 16, 2, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (23, 20, 22, 1);
        INSERT INTO public.autoria (id, livro_id, autor_id, ordem) VALUES (24, 8, 7, 1);
        INSERT INTO public.categoria (id, nome, descricao, ativa, data_criacao, data_atualizacao) VALUES (1, 'Tecnologia', 'Programação, arquitetura e engenharia de software.', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.categoria (id, nome, descricao, ativa, data_criacao, data_atualizacao) VALUES (2, 'Fantasia', 'Narrativas com elementos fantásticos.', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.categoria (id, nome, descricao, ativa, data_criacao, data_atualizacao) VALUES (3, 'Distopia', 'Ficção sobre sociedades autoritárias ou degradadas.', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.categoria (id, nome, descricao, ativa, data_criacao, data_atualizacao) VALUES (4, 'Romance', 'Obras de ficção em prosa.', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.categoria (id, nome, descricao, ativa, data_criacao, data_atualizacao) VALUES (5, 'Literatura Brasileira', 'Obras de autores brasileiros.', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.categoria (id, nome, descricao, ativa, data_criacao, data_atualizacao) VALUES (6, 'Infantojuvenil', 'Livros voltados ao público infantil e juvenil.', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.categoria (id, nome, descricao, ativa, data_criacao, data_atualizacao) VALUES (7, 'Ficção Científica', 'Ficção baseada em ciência e tecnologia.', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.categoria (id, nome, descricao, ativa, data_criacao, data_atualizacao) VALUES (8, 'História', 'Obras sobre acontecimentos e sociedades humanas.', true, '2026-11-08 14:41:16.339602', '2026-11-08 14:41:16.339602');
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (7, 7);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (19, 1);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (15, 8);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (7, 3);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (3, 1);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (9, 4);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (14, 7);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (10, 5);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (4, 1);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (6, 2);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (13, 7);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (16, 1);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (8, 5);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (12, 2);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (21, 4);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (1, 1);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (17, 1);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (10, 4);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (2, 1);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (9, 5);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (18, 1);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (8, 4);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (20, 4);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (12, 6);
        INSERT INTO public.livro_categoria (livro_id, categoria_id) VALUES (11, 6);
    END IF;
END
$dados_demonstracao$;

SELECT setval(
        pg_get_serial_sequence('public.endereco', 'id'),
        COALESCE(MAX(id), 1),
        MAX(id) IS NOT NULL)
FROM public.endereco;

SELECT setval(
        pg_get_serial_sequence('public.biblioteca', 'id'),
        COALESCE(MAX(id), 1),
        MAX(id) IS NOT NULL)
FROM public.biblioteca;

SELECT setval(
        pg_get_serial_sequence('public.livro', 'id'),
        COALESCE(MAX(id), 1),
        MAX(id) IS NOT NULL)
FROM public.livro;

SELECT setval(
        pg_get_serial_sequence('public.acervo', 'id'),
        COALESCE(MAX(id), 1),
        MAX(id) IS NOT NULL)
FROM public.acervo;

SELECT setval(
        pg_get_serial_sequence('public.autor', 'id'),
        COALESCE(MAX(id), 1),
        MAX(id) IS NOT NULL)
FROM public.autor;

SELECT setval(
        pg_get_serial_sequence('public.autoria', 'id'),
        COALESCE(MAX(id), 1),
        MAX(id) IS NOT NULL)
FROM public.autoria;

SELECT setval(
        pg_get_serial_sequence('public.categoria', 'id'),
        COALESCE(MAX(id), 1),
        MAX(id) IS NOT NULL)
FROM public.categoria;
