package br.com.livraria.desapega_livros.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.livraria.desapega_livros.entities.Idioma;

@Repository
public interface IdiomaRepository extends JpaRepository<Idioma, Integer> {

	boolean existsByNomeIgnoreCase(String nome);

}
