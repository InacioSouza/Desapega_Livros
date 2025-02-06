package br.com.livraria.desapega_livros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.livraria.desapega_livros.repository.entity.Editora;

@Repository
public interface EditoraRepository extends JpaRepository<Editora, Integer> {

}
