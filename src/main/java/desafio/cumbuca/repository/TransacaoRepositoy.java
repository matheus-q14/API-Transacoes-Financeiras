package desafio.cumbuca.repository;

import desafio.cumbuca.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepositoy extends JpaRepository<Transacao, Long> {
}
