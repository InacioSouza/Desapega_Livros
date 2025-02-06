package br.com.livraria.desapega_livros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.livraria.desapega_livros.repository.entity.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {

	@Query("""
			SELECT e.id
			FROM Endereco e
			WHERE e.cep = :cep AND e.numero = :numero
			""")
	Integer existsByCepAndNumero(String cep, Integer numero);

}
