package br.com.livraria.desapega_livros.repositories;

import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.livraria.desapega_livros.entities.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Repository
public interface AutorRepository
		extends BaseRepository<Autor, Integer> {

	boolean existsByNome(@NotBlank @NotNull String nome);

	Autor findByNome(String nome);

}
