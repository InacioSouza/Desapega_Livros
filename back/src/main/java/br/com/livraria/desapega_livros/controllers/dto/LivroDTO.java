package br.com.livraria.desapega_livros.controllers.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.livraria.desapega_livros.repository.entity.Livro;

public record LivroDTO(Integer id, String titulo, String status, String subtitulo, String descricao, String opniaoDoador,
		Integer qtdPaginas, String isbn, Integer anoPublicacao, @JsonProperty("editora") EditoraDTO editoraDTO,
		@JsonProperty("idioma") IdiomaDTO idiomaDTO, UsuarioDTO dono, List<CategoriaDTO> categorias,
		List<AutorDTO> autores) {

	public LivroDTO(Livro livro) {

		this(livro.getId(), livro.getTitulo(), livro.getStatus(), livro.getSubtitulo(), livro.getDescricao(), livro.getOpniaoDoador(),
				livro.getQtdPaginas(), livro.getIsbn(), livro.getAnoPublicacao(), new EditoraDTO(livro.getEditora()),
				new IdiomaDTO(livro.getIdioma()), new UsuarioDTO(livro.getDono()),
				livro.getCategorias().stream().map(categoria -> new CategoriaDTO(categoria))
						.collect(Collectors.toList()),
				livro.getAutores().stream().map(autor -> new AutorDTO(autor)).collect(Collectors.toList()));
	}

}
