package desafio.cumbuca.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transacao")
public class TransacaoController {

    @Operation(summary = "Teste de autenticação",
            security = {@SecurityRequirement(name = "bearer-key")}
    )
    @GetMapping("/transferir")
    public ResponseEntity<String> realizarTrasacao() {
        return ResponseEntity.status(HttpStatus.OK).body("Autenticacao funcionou");
    }

}
