package br.com.livraria.desapega_livros.entities;

import br.com.livraria.desapega_livros.controllers.form.AutorFORM;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	public Autor(AutorFORM autorForm) {
		this.nome = autorForm.nome().trim();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;

	public void atualizarDados(AutorFORM autorForm) {
		this.nome = autorForm.nome();
	}

	public Autor(String nome) {
		this.nome = nome;
	}
}
