package br.com.livraria.desapega_livros.repositories;

import br.com.livraria.desapega_livros.entities.Idioma;
import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdiomaRepository
		extends BaseRepository<Idioma, Integer> {

	boolean existsByNomeIgnoreCase(String nome);

}
