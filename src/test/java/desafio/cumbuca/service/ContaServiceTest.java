package desafio.cumbuca.service;

import desafio.cumbuca.dtos.CriarContaDto;
import desafio.cumbuca.exception.CreationErrorException;
import desafio.cumbuca.model.Conta;
import desafio.cumbuca.repository.ContaRepository;
import desafio.cumbuca.security.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

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
    void autenticarConta() {
    }
}