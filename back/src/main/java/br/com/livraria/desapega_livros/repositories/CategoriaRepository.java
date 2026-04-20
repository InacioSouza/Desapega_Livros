package br.com.livraria.desapega_livros.repositories;

import br.com.livraria.desapega_livros.entities.Categoria;
import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository
		extends BaseRepository<Categoria, Integer> {

	boolean existsByNome(String nome);

	Categoria findByNome(String nome);

	Categoria findByNome(Categoria nome);

}
