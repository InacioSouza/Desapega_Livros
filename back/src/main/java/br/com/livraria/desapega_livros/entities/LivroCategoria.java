package br.com.livraria.desapega_livros.entities;

import jakarta.persistence.Entity;
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
@Table(name = "livro_categoria")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LivroCategoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "id_livro")
	private Livro livro;

	@OneToOne
	@JoinColumn(name = "id_categoria")
	private Categoria categoria;
}
