package br.com.livraria.desapega_livros.repository.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
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
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

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
	@PrimaryKeyJoinColumn(name = "id_endereco")
	private Endereco endereco;

	private String senha;
	private int advertencias;
	private String status;
}
