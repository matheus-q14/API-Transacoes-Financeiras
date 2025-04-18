package desafio.cumbuca.service.interfaces;

import desafio.cumbuca.dtos.AutenticarContaDto;
import desafio.cumbuca.dtos.CriarContaDto;
import desafio.cumbuca.dtos.JwtTokenDto;

public interface ContaService {

    void criarConta(CriarContaDto criarContaDto);

    JwtTokenDto autenticarConta(AutenticarContaDto autenticarContaDto);
}
