package com.backend.sade.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CryptoPriceDTO {
    private String symbol; // Örnek: "BNB/BTC"
    private BigDecimal priceBtc; // BTC cinsinden fiyat
    private BigDecimal priceUsdt; // USDT cinsinden fiyat
    private LocalDateTime timestamp; // Fiyatın alındığı zaman
}