package com.vietqradminbe.infrastructure.configuration.security;

import com.vietqradminbe.domain.models.Account;
import com.vietqradminbe.domain.repositories.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class UserDetailsServiceConfig {

    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepository) {
        return username -> {
            Account account = accountRepository.getAccountByUsername(username);
            if (account == null) {
                throw new UsernameNotFoundException("Account not found");
            }
            return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getPassword(), List.of(new SimpleGrantedAuthority(account.getRole())));
        };
    }
}
