package com.dev.user_manage.service;

import com.dev.user_manage.dto.AuthUser;
import com.dev.user_manage.dto.RegisterUser;
import com.dev.user_manage.entity.Role;
import com.dev.user_manage.entity.User;
import com.dev.user_manage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.example.model.UserCreatedEvent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;

import org.joda.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${app.kafka.user-creation-topic}")
    private String userCreationTopic;
    private final PasswordEncoder passwordEncoder;
    private final com.dev.user_manage.service.JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public User register(RegisterUser registerUser){
        User user = mapToUser(registerUser);

        User savedUser = userRepository.save(user);

        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(savedUser.getUserId())
                .createdAt(Instant.now())
                .build();
        kafkaTemplate.send(userCreationTopic, event);

        return savedUser;
    }

    public String auth(AuthUser authUser) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword())
        );

        User userDetails = (User) userDetailsService.loadUserByUsername(authUser.getUsername());

        return jwtService.generateJwtToken(userDetails);
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private User mapToUser(RegisterUser registerUser){
        return User.builder()
                .username(registerUser.getUsername())
                .firstname(registerUser.getFirstname())
                .lastname(registerUser.getLastname())
                .password(passwordEncoder.encode(registerUser.getPassword()))
                .roles(List.of(Role.ROLE_USER))
                .build();
    }


}
