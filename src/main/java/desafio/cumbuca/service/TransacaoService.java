package desafio.cumbuca.service;

import desafio.cumbuca.dtos.RealizarTransacaoRequestDto;
import desafio.cumbuca.dtos.TransacaoPorDataRequestDto;
import desafio.cumbuca.dtos.TransacaoRealizadaResponseDto;
import desafio.cumbuca.dtos.TransacoesDataResponseDto;
import desafio.cumbuca.exception.InsufficientBalanceException;
import desafio.cumbuca.model.Conta;
import desafio.cumbuca.model.ContaDetailsImpl;
import desafio.cumbuca.model.Transacao;
import desafio.cumbuca.repository.ContaRepository;
import desafio.cumbuca.repository.TransacaoRepositoy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransacaoService {

    private static final Logger logger = LoggerFactory.getLogger(TransacaoService.class);

    @Autowired
    private TransacaoRepositoy transacaoRepositoy;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ContaService contaService;

    public TransacaoRealizadaResponseDto realizarTransacao(RealizarTransacaoRequestDto transacaoDto) {
        ContaDetailsImpl contaAutenticada = (ContaDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Conta contaADebitar = contaRepository.findByCpf(contaAutenticada.getUsername()).get();
        Conta contaACreditar = contaRepository.findByCpf(transacaoDto.cpfContaACreditar()).
                orElseThrow(() -> new RuntimeException("Conta a creditar não encontrada"));
        if (contaService.temSaldoSuficiente(contaADebitar, transacaoDto.valor()) && contaACreditar != null) {
            contaService.diminuirSaldo(contaADebitar, transacaoDto.valor());
            contaService.incrementarSaldo(contaACreditar, transacaoDto.valor());
            Transacao transacao = new Transacao(
                    new BigDecimal(transacaoDto.valor()),
                    LocalDate.now(),
                    contaADebitar,
                    contaACreditar
            );
            transacaoRepositoy.save(transacao);
            return new TransacaoRealizadaResponseDto(
                    contaADebitar.getNomeCompleto(),
                    contaACreditar.getNomeCompleto(),
                    transacaoDto.valor(),
                    LocalDate.now()
            );
        } else {
            throw new InsufficientBalanceException("Conta sem saldo o suficiente para transação");
        }
    }

    public List<TransacoesDataResponseDto> buscaPorData(TransacaoPorDataRequestDto requestDto) {
        List<TransacoesDataResponseDto> listaTransacoes = transacaoRepositoy.
                findAllByDataProcessamentoBetween(requestDto.dataInicial(), requestDto.dataFinal())
                .stream().map(transacao -> new TransacoesDataResponseDto(
                        transacao.getDataProcessamento(),
                        transacao.getValor().toString(),
                        transacao.getContaADebitar().getNomeCompleto(),
                        transacao.getContaACreditar().getNomeCompleto()
                )).toList();
        return listaTransacoes;
    }
}
