package br.com.livraria.desapega_livros.repositories;

import br.com.livraria.desapega_livros.entities.Editora;
import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditoraRepository
		extends BaseRepository<Editora, Integer> {

	boolean existsByNome(String nome);

	Editora findByNome(String editora);

}
