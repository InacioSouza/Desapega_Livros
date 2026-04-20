package br.com.livraria.desapega_livros.repositories;

import br.com.livraria.desapega_livros.entities.Cidade;
import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepository
		extends BaseRepository<Cidade, Integer> {

	Boolean existsByNomeIgnoreCase(String nome);

	Cidade findByNome(String localidade);

}
