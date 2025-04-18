package desafio.cumbuca.controller;

import desafio.cumbuca.dtos.AutenticarContaDto;
import desafio.cumbuca.dtos.CriarContaDto;
import desafio.cumbuca.dtos.JwtTokenDto;
import desafio.cumbuca.service.ContaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    private ContaServiceImpl contaService;

    @Operation(summary = "Criar uma nova conta",
            description = "Criar uma nova conta a partir do dto de criação de conta e salva no banco de dados")
    @PostMapping("/criar")
    public ResponseEntity<?> criarConta(@RequestBody CriarContaDto criarContaDto) {
        contaService.criarConta(criarContaDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> fazerLogin(@RequestBody AutenticarContaDto autenticarContaDto) {
        JwtTokenDto jwtToken = contaService.autenticarConta(autenticarContaDto);
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }
}
