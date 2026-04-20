package br.com.livraria.desapega_livros.controllers.bases;

import br.com.livraria.desapega_livros.services.bases.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

public abstract class BaseController<T, ID> {

    private BaseService<T, ID>  service;

    public BaseController(BaseService<T, ID>  service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<T>> findWithPagination(
            @PageableDefault(size = 10, sort = { "id" }) Pageable pagina) {
        return ResponseEntity.ok(this.service.findWithPagination(pagina));
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> findById(ID id) {
        return ResponseEntity.ok(this.service.findById(id));
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity.HeadersBuilder<?> simpleDelete(ID id) {
        this.service.simpleDeleteById(id);
        return ResponseEntity.noContent();
    }
}
