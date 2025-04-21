package desafio.cumbuca.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StandardError {

    private int statusCode;
    private String message;

}
