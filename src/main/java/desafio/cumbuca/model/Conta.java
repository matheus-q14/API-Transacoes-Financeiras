package desafio.cumbuca.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome")
    @NonNull
    private String nomeCompleto;
    @Column(unique = true)
    @NonNull
    private String cpf;
    @NonNull
    private String senha;
    @NonNull
    private BigDecimal saldo;
    @NonNull
    private LocalDate dataCriacao;
}
