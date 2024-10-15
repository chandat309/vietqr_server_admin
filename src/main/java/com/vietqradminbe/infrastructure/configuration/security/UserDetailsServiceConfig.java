//package com.vietqradminbe.infrastructure.configuration.security;
//
//import com.vietqradminbe.domain.models.Role;
//import com.vietqradminbe.domain.models.User;
//import com.vietqradminbe.domain.repositories.RoleRepository;
//import com.vietqradminbe.domain.repositories.UserRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Configuration
//public class UserDetailsServiceConfig {
//
//    @Bean
//    public UserDetailsService userDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
//        return username -> {
//            User user = userRepository.getUserByUsername(username);
//            if (user == null) {
//                throw new UsernameNotFoundException("User not found");
//            }
//
//            List<GrantedAuthority> authorities = roleRepository.getRolesByUsername(username).stream()
//                    .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
//                    .collect(Collectors.toList());
//            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPasswordHash(), authorities);
//        };
//    }
//}
