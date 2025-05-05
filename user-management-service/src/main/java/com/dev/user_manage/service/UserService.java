package com.dev.user_manage.service;

import com.dev.user_manage.dto.AuthUser;
import com.dev.user_manage.dto.RegisterUser;
import com.dev.user_manage.dto.UpdateUser;
import com.dev.user_manage.entity.Role;
import com.dev.user_manage.entity.User;
import com.dev.user_manage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public User register(RegisterUser registerUser){
        User user = mapToUser(registerUser);
        return userRepository.save(user);
    }

    public String auth(AuthUser authUser) {
        // Primero autenticar (esto lanzará una excepción si falla)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword())
        );
        // Luego generar el token
        return jwtService.generateJwtToken(authUser.getUsername());
    }

    private User mapToUser(RegisterUser registerUser){
        return User.builder()
                .username(registerUser.getUsername())
                .firstname(registerUser.getFirstname())
                .lastname(registerUser.getLastname())
                .password(passwordEncoder.encode(registerUser.getPassword()))
                .roles(List.of(Role.USER.name()))
                .build();
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
}
