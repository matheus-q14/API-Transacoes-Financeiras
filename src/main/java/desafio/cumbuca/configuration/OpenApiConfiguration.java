package desafio.cumbuca.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Desenvolvimento");

        Contact mwuContato = new Contact();
        mwuContato.setName("Matheus Querino");
        mwuContato.setEmail("matheusquerino91@gmail.com");

        Info information = new Info()
                .title("Desafio Cumbuca API")
                .version("1.0")
                .description("API para o dessafio back end Cumbuca, basicamente uma API de transações financeiras")
                .contact(mwuContato);

        Components components = new Components()
                .addSecuritySchemes("bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
        return new OpenAPI().info(information).servers(List.of(server)).components(components);
    }
}