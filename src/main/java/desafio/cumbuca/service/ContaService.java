package desafio.cumbuca.service;

import desafio.cumbuca.dtos.AutenticarContaDto;
import desafio.cumbuca.dtos.CriarContaDto;
import desafio.cumbuca.dtos.JwtTokenDto;
import desafio.cumbuca.dtos.SaldoResponseDto;
import desafio.cumbuca.exception.CreationErrorException;
import desafio.cumbuca.model.Conta;
import desafio.cumbuca.model.ContaDetailsImpl;
import desafio.cumbuca.repository.ContaRepository;
import desafio.cumbuca.security.SecurityConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

@Service
public class ContaService {

    private final Logger logger = LoggerFactory.getLogger(ContaService.class);

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    public void criarConta(CriarContaDto criarContaDto) {
        try {
            logger.info("Criando nova conta...");
            Conta novaConta = new Conta(
                    criarContaDto.nomeCompleto(),
                    criarContaDto.cpf(),
                    securityConfiguration.passwordEncoder().encode(criarContaDto.senha()),
                    new BigDecimal(criarContaDto.saldo()),
                    LocalDate.now());
            contaRepository.save(novaConta);
            logger.info("Conta criada com sucesso");
        } catch (Exception e) {
            throw new CreationErrorException("Erro ao criar uma nova conta, há informações faltando");
        }
    }

    public JwtTokenDto autenticarConta(AutenticarContaDto autenticarContaDto) {
        // Objeto para autenticar com Spring Security
        logger.info("Iniciando autenticação");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                autenticarContaDto.cpf(),
                autenticarContaDto.senha(),
                Collections.emptyList()
        );
        // Autentica o usuario
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        ContaDetailsImpl contaDetails = (ContaDetailsImpl) authentication.getPrincipal();
        // Constrói e retorna o token a partir da conta autenticada
        logger.info("Usuário autenticado com sucesso");
        return new JwtTokenDto(jwtTokenService.generateToken(contaDetails));
    }

    // verifica se há saldo para transação
    public boolean temSaldoSuficiente(Conta contaADebitar, String valorString) {
        BigDecimal valorADebitar = new BigDecimal(valorString);
        return contaADebitar.getSaldo().subtract(valorADebitar).compareTo(new BigDecimal("0")) >= 0;
    }

    public void incrementarSaldo(Conta conta, String valorString) {
        BigDecimal valorACreditar = new BigDecimal(valorString);
        BigDecimal novoSaldo = conta.getSaldo().add(valorACreditar);
        conta.setSaldo(novoSaldo);
        contaRepository.save(conta);
    }

    public void diminuirSaldo(Conta conta, String valorString) {
        BigDecimal valorADebitar = new BigDecimal(valorString);
        BigDecimal novoSaldo = conta.getSaldo().subtract(valorADebitar);
        conta.setSaldo(novoSaldo);
        contaRepository.save(conta);
    }

    public SaldoResponseDto consultarSaldo() {
        ContaDetailsImpl contaAutenticada = (ContaDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Conta conta = contaRepository.findByCpf(contaAutenticada.getUsername()).get();
        return new SaldoResponseDto(conta.getNomeCompleto(), conta.getSaldo().toString());
    }
}
