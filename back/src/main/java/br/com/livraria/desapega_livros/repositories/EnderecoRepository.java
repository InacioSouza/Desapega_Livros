package br.com.livraria.desapega_livros.repositories;

import br.com.livraria.desapega_livros.entities.Endereco;
import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository
		extends BaseRepository<Endereco, Integer> {

	@Query("""
			SELECT e.id
			FROM Endereco e
			WHERE e.cep = :cep AND e.numero = :numero
			""")
	Integer existsByCepAndNumero(String cep, Integer numero);

}
