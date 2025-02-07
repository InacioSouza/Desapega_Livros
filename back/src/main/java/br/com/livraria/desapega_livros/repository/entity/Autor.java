package br.com.livraria.desapega_livros.repository.entity;

import java.util.List;

import br.com.livraria.desapega_livros.controllers.form.AutorFORM;
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

	public Autor(AutorFORM autorForm) {
		this.nome = autorForm.nome().trim();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "livro_autor", joinColumns = @JoinColumn(name = "id_autor"), inverseJoinColumns = @JoinColumn(name = "id_livro"))
	private List<Livro> livros;

	public void atualizarDados(AutorFORM autorForm) {
		this.nome = autorForm.nome();
	}
}
