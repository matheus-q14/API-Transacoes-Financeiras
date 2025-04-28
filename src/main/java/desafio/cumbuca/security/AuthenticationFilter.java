package desafio.cumbuca.security;

import desafio.cumbuca.model.Conta;
import desafio.cumbuca.model.ContaDetailsImpl;
import desafio.cumbuca.repository.ContaRepository;
import desafio.cumbuca.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!checkEndpointIsPublic(request)) {
            logger.info("Recuperando token");
            String token = recoveryToken(request);
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token);
                Conta conta = contaRepository.findByCpf(subject).get();
                ContaDetailsImpl contaDetails = new ContaDetailsImpl(conta);
                Authentication authentication = new UsernamePasswordAuthenticationToken(contaDetails, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("User autenticado?: " + SecurityContextHolder.getContext().getAuthentication());
            } else {
                throw new RuntimeException(String.format("%s Sem token de autenticação", request.getRequestURI()));
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        // TODO colocar recuperar token pelos cookies
        return null;
    }


    private boolean checkEndpointIsPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        logger.info("Debugando requestURI");
        logger.info(requestURI);
        return Arrays.stream(SecurityConfiguration.ENDPOINTS_WITH_NO_AUTHENTICATION).anyMatch(requestURI::startsWith);
    }

}
