package br.com.livraria.desapega_livros.repositories;

import br.com.livraria.desapega_livros.entities.Estado;
import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository
		extends BaseRepository<Estado, Integer> {
	Boolean existsByNomeIgnoreCase(String nome);

	Estado findByNome(String estado);
}
