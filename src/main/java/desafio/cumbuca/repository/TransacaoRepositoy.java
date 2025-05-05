package desafio.cumbuca.repository;

import desafio.cumbuca.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepositoy extends JpaRepository<Transacao, Long> {
    List<Transacao> findAllByDataProcessamentoBetween(LocalDate dataInicial, LocalDate dataFinal);
}
