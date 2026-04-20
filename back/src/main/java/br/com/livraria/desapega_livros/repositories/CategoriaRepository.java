package br.com.livraria.desapega_livros.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.livraria.desapega_livros.entities.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

	boolean existsByNome(String nome);

	Categoria findByNome(String nome);

	Categoria findByNome(Categoria nome);

}
