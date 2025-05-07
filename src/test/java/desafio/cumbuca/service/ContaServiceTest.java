package desafio.cumbuca.service;

import desafio.cumbuca.dtos.AutenticarContaDto;
import desafio.cumbuca.dtos.CriarContaDto;
import desafio.cumbuca.dtos.JwtTokenDto;
import desafio.cumbuca.exception.CreationErrorException;
import desafio.cumbuca.model.Conta;
import desafio.cumbuca.model.ContaDetailsImpl;
import desafio.cumbuca.repository.ContaRepository;
import desafio.cumbuca.security.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SecurityConfiguration securityConfiguration;

    @Mock
    private Authentication authentication;

    @Mock
    private ContaDetailsImpl contaDetails;

    @Test
    void deveLancarExcecaoDeErroAoCriarConta() {
        CriarContaDto criarContaDto = mock(CriarContaDto.class);
        when(criarContaDto.senha()).thenReturn(null);

        CreationErrorException excecaoLancada = assertThrows(CreationErrorException.class, () -> {
            contaService.criarConta(criarContaDto);
        });

        assertEquals("Erro ao criar uma nova conta, há informações faltando", excecaoLancada.getMessage());

        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    void deveCriarContaComSucesso() {
        when(securityConfiguration.passwordEncoder()).thenReturn(new BCryptPasswordEncoder());
        CriarContaDto criarContaDto = new CriarContaDto("Matheus Querino", "12345678901", "senha1234", "1000.90");
        contaService.criarConta(criarContaDto);

        ArgumentCaptor<Conta> captor = ArgumentCaptor.forClass(Conta.class);

        // verificar se o metodo de salvar foi chamado
        verify(contaRepository).save(captor.capture());

        Conta contaSalva = captor.getValue();

        // Verifica se a conta salva possui as informações corretas
        assertEquals("Matheus Querino", contaSalva.getNomeCompleto());
        assertEquals("12345678901", contaSalva.getCpf());
    }

    @Test
    void deveAutenticarContaERetornarUmTokenJwt() {
        AutenticarContaDto autenticarContaDto = new AutenticarContaDto("12345678901", "senha1234");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(contaDetails);
        when(jwtTokenService.generateToken(contaDetails)).thenReturn("token.jwt.falso");


        JwtTokenDto tokenDto = contaService.autenticarConta(autenticarContaDto);

        // verifica se retornou um jwt
        assertEquals("token.jwt.falso", tokenDto.jwtToken());
    }
}