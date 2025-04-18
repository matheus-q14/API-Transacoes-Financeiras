package desafio.cumbuca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal valor;
    private LocalDate dataProcessamento;
    @ManyToOne
    @JoinColumn(name = "conta_debitar_id", nullable = false)
    private Conta contaADebitar;
    @ManyToOne
    @JoinColumn(name = "conta_creditar_id", nullable = false)
    private Conta contaACreditar;
}
