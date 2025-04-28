package desafio.cumbuca.dtos;

import java.time.LocalDate;

public record TransacaoRealizadaResponseDto(String titularContaDebitada,
                                            String titularContaCreditada,
                                            String valorTransacao,
                                            LocalDate dataProcessamento) {
}
