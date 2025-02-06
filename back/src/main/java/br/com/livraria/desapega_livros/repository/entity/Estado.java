package br.com.livraria.desapega_livros.repository.entity;

import br.com.livraria.desapega_livros.controllers.form.EstadoFORM;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "estado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Estado {

	public Estado(EstadoFORM estadoForm) {
		this.nome = estadoForm.nome();
		this.uf = estadoForm.uf().toUpperCase();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;
	private String uf;
}
