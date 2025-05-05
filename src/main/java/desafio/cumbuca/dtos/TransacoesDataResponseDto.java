package desafio.cumbuca.dtos;

import java.time.LocalDate;

public record TransacoesDataResponseDto(
        LocalDate data,
        String valor,
        String titularContaDebitada,
        String titularContaCreditada
) {
}
