package br.com.livraria.desapega_livros.repositories;

import br.com.livraria.desapega_livros.entities.Categoria;
import br.com.livraria.desapega_livros.entities.Livro;
import br.com.livraria.desapega_livros.entities.LivroCategoria;
import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LivroCategoriaRepository
		extends BaseRepository<LivroCategoria, Integer> {

	@Query("""
			SELECT EXISTS(
				SELECT 1
				FROM LivroCategoria lc
				WHERE lc.livro = :livro
				AND lc.categoria = :categoria
			)
			""")
	boolean existsLivroCategoria(Livro livro, Categoria categoria);

	@Modifying
	@Query("""
			DELETE FROM LivroCategoria lc
			WHERE lc.livro.id = :idLivro AND lc.categoria.id = :idCategoria
			""")
	void removePorIdLivroEIdCategoria(Integer idLivro, Integer idCategoria);

}
