package desafio.cumbuca.service;

import desafio.cumbuca.dtos.AutenticarContaDto;
import desafio.cumbuca.dtos.CriarContaDto;
import desafio.cumbuca.dtos.JwtTokenDto;
import desafio.cumbuca.model.Conta;
import desafio.cumbuca.model.ContaDetailsImpl;
import desafio.cumbuca.repository.ContaRepository;
import desafio.cumbuca.security.SecurityConfiguration;
import desafio.cumbuca.service.interfaces.ContaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ContaServiceImpl implements ContaService {

    private Logger logger = LoggerFactory.getLogger(ContaServiceImpl.class);

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Override
    public void criarConta(CriarContaDto criarContaDto) {
        logger.info("Criando nova conta...");
        Conta novaConta = new Conta(
                criarContaDto.nomeCompleto(),
                criarContaDto.cpf(),
                securityConfiguration.passwordEncoder().encode(criarContaDto.senha()),
                new BigDecimal(criarContaDto.saldo()),
                LocalDate.now());
        contaRepository.save(novaConta);
        logger.info("Conta criada com sucesso");
    }

    @Override
    public JwtTokenDto autenticarConta(AutenticarContaDto autenticarContaDto) {
        // Objeto para autenticar com Spring Security
        logger.info("Iniciando autenticação");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                autenticarContaDto.cpf(),
                autenticarContaDto.senha()
        );
        // Autentica o usuario
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        ContaDetailsImpl contaDetails = (ContaDetailsImpl) authentication.getPrincipal();
        // Constrói e retorna o token a partir da conta autenticada
        logger.info("Usuário autenticado com sucesso");
        return new JwtTokenDto(jwtTokenService.generateToken(contaDetails));
    }
}
