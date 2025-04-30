package br.com.livraria.desapega_livros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.livraria.desapega_livros.repository.entity.Autor;
import br.com.livraria.desapega_livros.repository.entity.Livro;
import br.com.livraria.desapega_livros.repository.entity.LivroAutor;

public interface LivroAutorRepository extends JpaRepository<LivroAutor, Integer> {

	@Query("""
			SELECT EXISTS (
				SELECT 1
				FROM LivroAutor la
				WHERE la.autor = :autor
				AND la.livro = :livro
			)
			""")
	boolean existsLivroAutor(Autor autor, Livro livro);

	@Modifying
	@Query("""
			DELETE FROM LivroAutor la
			WHERE la.livro.id = :idLivro AND la.autor.id = :idAutor
			""")
	void removePorIdLivroEIdAutor(Integer idLivro, Integer idAutor);

}
