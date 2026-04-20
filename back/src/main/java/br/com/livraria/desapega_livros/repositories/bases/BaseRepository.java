package br.com.livraria.desapega_livros.repositories.bases;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {}
