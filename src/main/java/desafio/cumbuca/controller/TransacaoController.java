package desafio.cumbuca.controller;


import desafio.cumbuca.dtos.RealizarTransacaoRequestDto;
import desafio.cumbuca.dtos.TransacaoRealizadaResponseDto;
import desafio.cumbuca.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transacao")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @Operation(security = {@SecurityRequirement(name = "bearer-key")},
            summary = "Realiza um transferência",
            description = "Realiza uma transferência entre o usuário logado e a" +
                    " conta que foi passada no corpo da requisição caso haja saldo disponível",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transação realizada",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransacaoRealizadaResponseDto.class))}),
                    @ApiResponse(responseCode = "400", description = "Saldo insuficiente")
            }
    )
    @PostMapping("/transferir")
    public ResponseEntity<TransacaoRealizadaResponseDto> realizarTrasacao(@RequestBody RealizarTransacaoRequestDto transacaoDto) {
        return ResponseEntity.status(HttpStatus.OK).body(transacaoService.realizarTransacao(transacaoDto));
    }

}
