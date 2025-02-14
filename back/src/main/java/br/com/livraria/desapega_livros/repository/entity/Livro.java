package br.com.livraria.desapega_livros.repository.entity;

import java.time.LocalDate;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "livro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Livro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String titulo;
	// Adicionar atributo opcional subTitulo;
	private String descricao;

	@Column(name = "opniao_doador")
	private String opniaoDoador;

	@Column(name = "qtd_paginas")
	private Integer qtdPaginas;

	private String isbn;

	@Column(name = "data_publicacao")
	private LocalDate dataPublicacao;

	private byte[] capa;

	private String status;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_editora")
	private Editora editora;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_idioma")
	private Idioma idioma;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_dono")
	private Usuario dono;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cidade")
	private Cidade ciade;

	//Por alguma razão que eu não sei o JPA não está  pegando os registros de autor e categoria relacionados a livro do banco de dados
	@ManyToMany
	@JoinTable(name = "livro_categoria", joinColumns = @JoinColumn(name = "id_livro"), inverseJoinColumns = @JoinColumn(name = "id_categoria"))
	private List<Categoria> categorias;
	
	@ManyToMany
	@JoinTable(name = "livro_autor", joinColumns = @JoinColumn(name = "id_livro"), inverseJoinColumns = @JoinColumn(name = "id_autor"))
	private List<Autor> autores;

}
