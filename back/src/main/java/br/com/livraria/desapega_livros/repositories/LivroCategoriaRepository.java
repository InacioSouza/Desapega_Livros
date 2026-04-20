package br.com.livraria.desapega_livros.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.livraria.desapega_livros.entities.Categoria;
import br.com.livraria.desapega_livros.entities.Livro;
import br.com.livraria.desapega_livros.entities.LivroCategoria;

public interface LivroCategoriaRepository extends JpaRepository<LivroCategoria, Integer> {

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
