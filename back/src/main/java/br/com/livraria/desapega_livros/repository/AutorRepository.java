package br.com.livraria.desapega_livros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.livraria.desapega_livros.repository.entity.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer> {

	@Query("""
			SELECT EXISTS (
				SELECT 1
				FROM Autor aut
				WHERE aut.nome = :nome
				AND aut.sobrenome = :sobrenome
				AND aut.nacionalidade = :nacionalidade
			)
			""")
	boolean autorCadastrado(String nome, String sobrenome, String nacionalidade);

}
