package desafio.cumbuca.service;

import desafio.cumbuca.model.Conta;
import desafio.cumbuca.model.ContaDetailsImpl;
import desafio.cumbuca.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ContaDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ContaRepository contaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Conta conta = contaRepository.findByCpf(username).orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        return new ContaDetailsImpl(conta);
    }

}
