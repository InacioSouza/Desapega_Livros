package br.com.livraria.desapega_livros.controllers.publics;

import br.com.livraria.desapega_livros.controllers.dto.TokenDTO;
import br.com.livraria.desapega_livros.controllers.form.LoginFORM;
import br.com.livraria.desapega_livros.entities.Usuario;
import br.com.livraria.desapega_livros.services.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PrefixEndpoint.PREFIX_PUBLIC + "/public")
@Tag(name = "Public")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login( @Valid @RequestBody LoginFORM loginDTO) {

        var auth = new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.senha());

        var authVerificado = authenticationManager.authenticate(auth);

        String token = tokenService.generateToken((Usuario) authVerificado.getPrincipal());

        TokenDTO tokenDTO = new TokenDTO(token);

        return ResponseEntity.ok(tokenDTO);
    }
}
