package com.backend.sade.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // Kullanıcı adı

    @Column(nullable = false, unique = true)
    private String email; // E-posta

    @Column(nullable = false)
    private String password; // Şifre (hashlenmiş olmalı)

    @Column(nullable = true)
    private String firstName; // Ad

    @Column(nullable = true)
    private String lastName; // Soyad

    @Column(nullable = true)
    private String phone; // Telefon numarası

    @Column(nullable = true)
    private String address; // Adres

    @Column(nullable = true)
    private LocalDate birthDate; // Doğum tarihi

    @Column(nullable = false)
    private LocalDate registrationDate = LocalDate.now(); // Kayıt tarihi
}
