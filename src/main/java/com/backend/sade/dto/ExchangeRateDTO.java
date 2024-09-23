package com.backend.sade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor // No-arg constructor ekleniyor
@AllArgsConstructor // All-arg constructor ekleniyor
public class ExchangeRateDTO {
    private String currencyPair;
    private BigDecimal rate;
    private LocalDateTime timestamp;
}
