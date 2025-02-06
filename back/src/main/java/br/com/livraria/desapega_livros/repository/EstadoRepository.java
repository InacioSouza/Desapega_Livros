package br.com.livraria.desapega_livros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.livraria.desapega_livros.repository.entity.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
	Boolean existsByNomeIgnoreCase(String nome);

	Estado findByNome(String estado);
}
