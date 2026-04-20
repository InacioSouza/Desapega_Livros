package br.com.livraria.desapega_livros.repositories;

import br.com.livraria.desapega_livros.entities.Autor;
import br.com.livraria.desapega_livros.entities.Categoria;
import br.com.livraria.desapega_livros.entities.Livro;
import br.com.livraria.desapega_livros.entities.enuns.StatusLivro;
import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository
		extends BaseRepository<Livro, Integer> {

	@Query("""
			SELECT EXISTS (
				SELECT 1
				FROM Livro l
				WHERE l.titulo = :titulo
				AND l.dono.id = :idDono
				AND l.idioma.id = :idIdioma
			)
			""")
	boolean livroJaCadastrado(String titulo, Integer idDono, Integer idIdioma);

	@Query("""
			SELECT cat
			FROM Categoria cat
			WHERE cat.id IN (
				SELECT lc.categoria.id
				FROM LivroCategoria lc
				WHERE lc.livro.id = :id
			)
			""")
	List<Categoria> categoriasPorIdLivro(Integer id);

	@Query("""
			SELECT aut
			FROM Autor aut
			WHERE aut.id IN (
				SELECT la.autor.id
				FROM LivroAutor la
				WHERE la.livro.id = :id
			)
			""")
	List<Autor> autoresPorIdLivro(Integer id);

	@Query("""
			SELECT l.status
			FROM Livro l
			WHERE l.id = :id
			""")
	StatusLivro statusLivro(@NotNull Integer id);

}
