package com.backend.sade.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exchange_rate")
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String currencyPair; // Örnek: "USD/TRY"

    @Column(nullable = false)
    private BigDecimal rate; // Örnek: 7.75

    @Column(nullable = false)
    private LocalDateTime timestamp; // Fiyatın alındığı zaman
}
