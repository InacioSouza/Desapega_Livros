package br.com.livraria.desapega_livros.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.livraria.desapega_livros.entities.Editora;

@Repository
public interface EditoraRepository extends JpaRepository<Editora, Integer> {

	boolean existsByNome(String nome);

	Editora findByNome(String editora);

}
