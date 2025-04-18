package desafio.cumbuca.security;

import desafio.cumbuca.model.Conta;
import desafio.cumbuca.model.UserDetailsImpl;
import desafio.cumbuca.repository.ContaRepository;
import desafio.cumbuca.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class AuthenticationFilter extends OncePerRequestFilter {

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
                UserDetailsImpl contaDetails = new UserDetailsImpl(conta);
                Authentication authentication = new UsernamePasswordAuthenticationToken(contaDetails.getUsername(), contaDetails.getPassword());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new RuntimeException("Sem token de autenticação");
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
        return !Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_NO_AUTHENTICATION).contains(requestURI);
    }

}
