package br.com.livraria.desapega_livros.repository.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "livro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String titulo;
	private String descricao;

	@Column(name = "data_publicacao")
	private LocalDate dataPublicacao;

	private byte[] capa;

	private String status;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id_editora")
	private Editora editora;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id_idioma")
	private Idioma idioma;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id_dono")
	private Usuario dono;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "id_cidade")
	private Cidade ciade;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "livros")
	private List<Categoria> categorias;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "livros")
	private List<Autor> autores;
}
