package com.vietqradminbe.infrastructure.configuration.security;

import com.vietqradminbe.domain.models.User;
import com.vietqradminbe.domain.repositories.RoleRepository;
import com.vietqradminbe.domain.repositories.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Configuration
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }


        // Fetch user roles
        List<GrantedAuthority> authorities = roleRepository.getRolesByUsername(username).stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRoleName()))
                .collect(Collectors.toList());

        // Return Spring Security user with username, password, and authorities
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                authorities
        );
    }
}
