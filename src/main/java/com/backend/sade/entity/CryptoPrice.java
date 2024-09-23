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
@Table(name = "crypto_price")
public class CryptoPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol; // Örnek: "BNB/BTC", "ETH/BTC" gibi semboller

    @Column(name = "price_btc", nullable = false)
    private BigDecimal priceBtc; // BTC cinsinden fiyat,

    @Column(name = "price_usdt", nullable = false)
    private BigDecimal priceUsdt; // USDT cinsinden fiyat,

    @Column(nullable = false)
    private LocalDateTime timestamp; // Fiyatın alındığı zaman
}
