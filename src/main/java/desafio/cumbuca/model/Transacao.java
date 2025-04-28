package desafio.cumbuca.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private BigDecimal valor;
    @NonNull
    private LocalDate dataProcessamento;
    @ManyToOne
    @JoinColumn(name = "conta_debitar_id", nullable = false)
    @NonNull
    private Conta contaADebitar;
    @ManyToOne
    @JoinColumn(name = "conta_creditar_id", nullable = false)
    @NonNull
    private Conta contaACreditar;
}
