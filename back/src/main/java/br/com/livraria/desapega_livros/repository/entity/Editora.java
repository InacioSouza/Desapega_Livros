package br.com.livraria.desapega_livros.repository.entity;

import br.com.livraria.desapega_livros.controllers.form.EditoraFORM;
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
@Table(name = "editora")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Editora {
	public Editora(EditoraFORM editoraForm) {
		this.nome = editoraForm.nome();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;
}
