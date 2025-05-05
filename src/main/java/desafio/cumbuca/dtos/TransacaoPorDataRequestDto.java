package desafio.cumbuca.dtos;

import java.time.LocalDate;

public record TransacaoPorDataRequestDto(
        LocalDate dataInicial,
        LocalDate dataFinal
) {
}
