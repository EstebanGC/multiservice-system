package com.dev.user_manage.controller;

import com.dev.user_manage.dto.AuthUser;
import com.dev.user_manage.dto.RegisterUser;
import com.dev.user_manage.entity.User;
import com.dev.user_manage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterUser registerUser) {
        return ResponseEntity.ok(userService.register(registerUser));
    }

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody AuthUser authUser) {
        return ResponseEntity.ok(userService.auth(authUser));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
//
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN') or @userService.isUserOwner(#id, authentication.principal.username)")
//    public ResponseEntity<User> getUserById(@PathVariable String id) {
//        return ResponseEntity.ok(userService.getUserById(id));
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN') or @userService.isUserOwner(#id, authentication.principal.username)")
//    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UpdateUser updateUser) {
//        return ResponseEntity.ok(userService.updateUser(id, updateUser));
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
//        userService.deleteUser(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/{id}/change-password")
//    @PreAuthorize("hasAuthority('ADMIN') or @userService.isUserOwner(#id, authentication.principal.username)")
//    public ResponseEntity<Void> changePassword(
//            @PathVariable String id,
//            @RequestParam String oldPassword,
//            @RequestParam String newPassword) {
//        userService.changePassword(id, oldPassword, newPassword);
//        return ResponseEntity.ok().build();
//    }
}
