package br.com.livraria.desapega_livros.entities;

import br.com.livraria.desapega_livros.entities.enuns.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "solicitacoes")
@NamedQuery(
		name = "solicitacoes.status",
		query = "SELECT s FROM Solicitacao s WHERE s.status = :statusSolicitacao"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_livro")
	private Livro livro;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@Column(name = "data_solicitacao")
	private LocalDate dataSolicitacao;

	@Enumerated(EnumType.STRING)
	private StatusSolicitacao status;

}
