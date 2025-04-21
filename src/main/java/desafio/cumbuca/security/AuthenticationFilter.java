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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (checkEndpointNotPublic(request)) {
            String token = recoveryToken(request);
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token);
                Conta conta = contaRepository.findByCpf(subject).get();
                ContaDetailsImpl contaDetails = new ContaDetailsImpl(conta);
                Authentication authentication = new UsernamePasswordAuthenticationToken(contaDetails.getUsername(), contaDetails.getPassword());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new RuntimeException(String.format("%s Sem token de autenticação", request.getRequestURI()));
            }
            filterChain.doFilter(request, response);
        }
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }


    private boolean checkEndpointNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        logger.info("Debugando requestURI");
        logger.info(requestURI);
        return !Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_NO_AUTHENTICATION).contains(requestURI);
    }

}
