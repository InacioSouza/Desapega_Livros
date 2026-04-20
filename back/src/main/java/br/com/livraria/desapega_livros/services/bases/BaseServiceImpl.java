package br.com.livraria.desapega_livros.services.bases;

import br.com.livraria.desapega_livros.infra.exception.NenhumRegistroEncontradoException;
import br.com.livraria.desapega_livros.infra.exception.RegistroNaoExisteException;
import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    private BaseRepository baseRepository;

    public BaseServiceImpl(BaseRepository baseRepository) {}

    @Override
    public T findById(ID id) {
        Optional<T> optionalEntity = this.baseRepository.findById(id);
        if (optionalEntity.isEmpty()) return optionalEntity.get();
        return null;
    }

    @Override
    public Boolean existsById(ID id) {
        return this.baseRepository.existsById(id);
    }

    @Override
    public T simpleSave(T entity) {
        return (T) this.baseRepository.save(entity);
    }

    private void checksExistence(ID id) {
        Boolean exists = this.existsById(id);
        if (!exists) {
            throw new RegistroNaoExisteException("Não existe registro cadastrado para o id : " + id);
        }
    }
    @Override
    public void simpleDeleteById(ID id) {
        this.checksExistence(id);
        this.baseRepository.deleteById(id);
    }

    @Override
    public T simpleUpdate(ID id, T entity) {
        this.checksExistence(id);
        return (T) this.baseRepository.save(entity);
    }

    @Override
    public Page<T> findWithPagination(Pageable pageable) {

        Page<T> pageEntity = this.baseRepository.findAll(pageable);

        if (pageEntity.getNumberOfElements() == 0) {
            throw new NenhumRegistroEncontradoException("Nenhum registro encontrado no banco de dados!");
        }

        return pageEntity;
    }

    @Override
    public List<T> findWithoutPagination() {
        List<T> listEntity = this.baseRepository.findAll();

        if (listEntity.isEmpty()) {
            throw new NenhumRegistroEncontradoException("Nenhum registro encontrado no banco de dados!");
        }

        return listEntity;
    }
}
