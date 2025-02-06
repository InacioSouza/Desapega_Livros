package br.com.livraria.desapega_livros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.livraria.desapega_livros.repository.entity.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

	Boolean existsByNomeIgnoreCase(String nome);

	Cidade findByNome(String localidade);

}
