package br.com.livraria.desapega_livros.controllers.publics;

import br.com.livraria.desapega_livros.controllers.dto.UsuarioDTO;
import br.com.livraria.desapega_livros.controllers.form.UsuarioFORM;
import br.com.livraria.desapega_livros.services.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController()
@RequestMapping(PrefixEndpoint.PREFIX_PUBLIC +"/public")
@Tag(name = "Public")
public class CreateUserController {

    private UsuarioService service;

    public  CreateUserController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<?> cadastrar(
            @RequestBody
            @Valid
            UsuarioFORM usuarioForm ) {

        var usuario = this.service.cadastrar(usuarioForm);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        var uri = uriBuilder.path("/usuario/{id}")
                .buildAndExpand(usuario.getId()).toUri();

        return ResponseEntity.created(uri).body(new UsuarioDTO(usuario));
    }
}
