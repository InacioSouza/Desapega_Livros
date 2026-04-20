package br.com.livraria.desapega_livros.repositories;

import br.com.livraria.desapega_livros.entities.Endereco;
import br.com.livraria.desapega_livros.entities.Usuario;
import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository
		extends BaseRepository<Usuario, Integer> {

	@Query("""
			SELECT u.endereco
			FROM Usuario u
			WHERE u.id = :id
			""")
	Endereco getEnderecoUsuario(Integer id);

	boolean existsByEmail(String email);

	UserDetails findByEmail(String email);

}
