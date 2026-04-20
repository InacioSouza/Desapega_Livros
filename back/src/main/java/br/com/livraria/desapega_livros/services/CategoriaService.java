package br.com.livraria.desapega_livros.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.livraria.desapega_livros.controllers.dto.CategoriaDTO;
import br.com.livraria.desapega_livros.controllers.form.CategoriaFORM;
import br.com.livraria.desapega_livros.infra.exception.NenhumRegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repositories.CategoriaRepository;
import br.com.livraria.desapega_livros.entities.Categoria;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepo;

	@Transactional
	public ResponseEntity<?> cadastrar(CategoriaFORM categoriaForm) {
		if (categoriaRepo.existsByNome(categoriaForm.nome())) {
			throw new RegistroEncontradoException(
					"A categoria " + categoriaForm.nome() + " já foi cadastrada no banco");
		}

		Categoria categoria = categoriaRepo.save(new Categoria(categoriaForm));
		CategoriaDTO categoriaSalvaDTO = new CategoriaDTO(categoria);

		UriComponentsBuilder uribuilder = UriComponentsBuilder.newInstance();
		var uri = uribuilder.path("/categoria/{id}").buildAndExpand(categoria.getId()).toUri();

		return ResponseEntity.created(uri).body(categoriaSalvaDTO);
	}

	@Transactional
	public ResponseEntity<?> listar(Pageable pagina) {

		var categorias = categoriaRepo.findAll(pagina).map(CategoriaDTO::new);

		if (categorias.getNumberOfElements() == 0) {
			throw new NenhumRegistroEncontradoException("Ainda não há nenhuma categoria cadastrada!");
		}

		return ResponseEntity.ok(categorias);

	}

	@Transactional
	public ResponseEntity<?> atualizar(Integer id, CategoriaFORM categoriaForm) {

		if (!categoriaRepo.existsById(id)) {
			throw new RegistroNaoExisteException("Não há nenhuma categoria cadastrada para o id " + id);
		}

		var categoria = categoriaRepo.findById(id).get();
		categoria.setNome(categoriaForm.nome());

		CategoriaDTO categoriaAtualizadaDTO = new CategoriaDTO(categoria);

		return ResponseEntity.ok(categoriaAtualizadaDTO);
	}

	@Transactional
	public ResponseEntity<?> excluir(Integer id) {

		if (!categoriaRepo.existsById(id)) {
			throw new RegistroNaoExisteException("Não há nenhuma categoria cadastrada para o id " + id);
		}

		categoriaRepo.deleteById(id);

		return ResponseEntity.ok("Categoria deletada com sucesso!");
	}
}
