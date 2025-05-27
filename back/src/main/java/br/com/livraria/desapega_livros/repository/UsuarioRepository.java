package br.com.livraria.desapega_livros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import br.com.livraria.desapega_livros.repository.entity.Endereco;
import br.com.livraria.desapega_livros.repository.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	@Query("""
			SELECT u.endereco
			FROM Usuario u
			WHERE u.id = :id
			""")
	Endereco getEnderecoUsuario(Integer id);

	boolean existsByEmail(String email);

	UserDetails findByEmail(String email);

}
