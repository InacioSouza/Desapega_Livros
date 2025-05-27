package br.com.livraria.desapega_livros.repository.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import br.com.livraria.desapega_livros.controllers.form.UsuarioFORM;
import br.com.livraria.desapega_livros.repository.entity.enuns.CategoriaEmum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

	public Usuario(UsuarioFORM usuarioForm) {
		this.nome = usuarioForm.nome();
		this.sobrenome = usuarioForm.sobrenome();
		this.dataNascimento = usuarioForm.dataNascimento();
		this.email = usuarioForm.email();
		this.whatsapp = usuarioForm.whatsapp();
		this.senha = usuarioForm.senha();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;
	private String sobrenome;

	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	private String email;
	private String whatsapp;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_endereco")
	private Endereco endereco;

	private String senha;

	private int advertencias;

	private String status;

	@Enumerated(value = EnumType.STRING)
	private CategoriaEmum categoria;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		if(this.categoria == CategoriaEmum.ADM){
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
					new SimpleGrantedAuthority("ROLE_USER"));
		}

		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.email;
	}
}
