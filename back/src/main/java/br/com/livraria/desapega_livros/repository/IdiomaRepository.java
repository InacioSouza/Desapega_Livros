package br.com.livraria.desapega_livros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.livraria.desapega_livros.repository.entity.Idioma;

@Repository
public interface IdiomaRepository extends JpaRepository<Idioma, Integer> {

}
