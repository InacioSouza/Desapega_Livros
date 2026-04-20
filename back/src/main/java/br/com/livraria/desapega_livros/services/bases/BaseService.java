package br.com.livraria.desapega_livros.services.bases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<T, ID> {

    T findById(ID id);

    Boolean existsById(ID id);

    /**
     * Apenas salva a Entidade sem fazer nenhuma verificação
     **/
    T simpleSave(T entity);

    /**
     * Apenas deleta a Entidade sem fazer nenhuma verificação
     **/
    void simpleDeleteById(ID id);

    /**
     * Apenas atualiza a Entidade sem fazer nenhuma verificação
     **/
    T simpleUpdate(ID id, T entity);

    Page<T> findWithPagination(Pageable pageable);

    List<T> findWithoutPagination();

}
