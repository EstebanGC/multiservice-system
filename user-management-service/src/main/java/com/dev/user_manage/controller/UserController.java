package com.dev.user_manage.controller;

import com.dev.user_manage.dto.AuthUser;
import com.dev.user_manage.dto.RegisterUser;
import com.dev.user_manage.entity.User;
import com.dev.user_manage.service.JwtService;
import com.dev.user_manage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterUser registerUser) {
        return ResponseEntity.ok(userService.register(registerUser));
    }

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody AuthUser authUser) {
        System.out.println("Username: " + authUser.getUsername());
        System.out.println("Password: " + authUser.getPassword());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authUser.getUsername(),
                        authUser.getPassword()
                )
        );

        User user = userService.loadUserByUsername(authUser.getUsername());
        String jwt = jwtService.generateJwtToken(user);

        return ResponseEntity.ok(jwt);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
