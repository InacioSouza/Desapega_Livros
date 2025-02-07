package br.com.livraria.desapega_livros.repository.entity;

import br.com.livraria.desapega_livros.controllers.dto.ViaCepResponseDTO;
import br.com.livraria.desapega_livros.controllers.form.EnderecoFORM;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Endereco {

	public Endereco(ViaCepResponseDTO dadosViaCep) {
		this.cep = dadosViaCep.cep().trim();
		this.logradouro = dadosViaCep.logradouro().trim();
		this.bairro = dadosViaCep.bairro().trim();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String cep;
	private int numero;
	private String logradouro;
	private String bairro;
	private String complemento;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cidade")
	private Cidade cidade;

}
