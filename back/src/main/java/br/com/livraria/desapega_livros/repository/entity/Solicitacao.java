package br.com.livraria.desapega_livros.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "solicitacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id_livro")
	private Livro livro;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id_usuario")
	private Usuario usuario;

	private String status;

}
