package br.com.livraria.desapega_livros.repositories;

import br.com.livraria.desapega_livros.entities.Solicitacao;
import br.com.livraria.desapega_livros.repositories.bases.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SolicitacaoRepository
        extends BaseRepository<Solicitacao, Integer> {

    //A tabela de solicitações deve ter um campo data, a query deve contar as solicitações de determinado usuário em um período de tempo
    @Query("""
            SELECT count(*) 
            FROM Solicitacao s
            WHERE s.usuario.id = :idUsuario 
            AND s.dataSolicitacao BETWEEN :primeiroDiaMes AND :ultimoDiaMes
            """)
    Integer solicitacoesNoPeriodo(Integer idUsuario, LocalDate primeiroDiaMes, LocalDate ultimoDiaMes);
}
