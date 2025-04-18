package desafio.cumbuca;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Task Manager API", description = "API de gerenciamento de tarefas"))
public class app {

    public static void main(String[] args) {
        SpringApplication.run(app.class, args);
    }

}
