package br.com.livraria.desapega_livros.repository.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "autor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Autor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;
	private String sobrenome;
	private String nacionalidade;

	@Column(name = "nome_artistico")
	private String nomeArtistico;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "livro_autor", joinColumns = @JoinColumn(name = "id_autor"), inverseJoinColumns = @JoinColumn(name = "id_livro"))
	private List<Livro> livros;
}
