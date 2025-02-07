package br.com.livraria.desapega_livros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.livraria.desapega_livros.repository.LivroRepository;

@Service
public class LivroService {

	@Autowired
	private LivroRepository livroRepo;

}
