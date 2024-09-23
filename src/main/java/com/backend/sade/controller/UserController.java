package com.backend.sade.controller;

import com.backend.sade.entity.User;
import com.backend.sade.service.UserService;
import com.backend.sade.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Kullanıcı bilgilerini getirme
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Kullanıcı profilini güncelleme
    @PutMapping("/profile")
    public ResponseEntity<User> updateUserProfile(@RequestBody User user, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> existingUser = userService.findByUsername(username);

        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            // Güncellenecek alanlar burada belirlenir
            updatedUser.setFirstName(user.getFirstName());
            updatedUser.setLastName(user.getLastName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPhone(user.getPhone());
            updatedUser.setAddress(user.getAddress());
            updatedUser.setBirthDate(user.getBirthDate());
            return ResponseEntity.ok(userService.saveUser(updatedUser));
        } else {
            return ResponseEntity.notFound().build(); // Kullanıcı bulunamazsa
        }
    }

    // Kullanıcıyı ID ile silme
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Başarılı silme işlemi
    }
}
