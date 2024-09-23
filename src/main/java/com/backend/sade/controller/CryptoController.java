package com.backend.sade.controller;

import com.backend.sade.dto.CryptoPriceDTO;
import com.backend.sade.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/crypto")
@Slf4j
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoPriceService) {
        this.cryptoService = cryptoPriceService;
    }

    // Son 60 dakikalık ortalama fiyat
    @GetMapping("/hourly-average")
    public ResponseEntity<BigDecimal> getHourlyAverage(@RequestParam String symbol) {
        BigDecimal averagePrice = cryptoService.getHourlyAverage(symbol);
        return ResponseEntity.ok(averagePrice);
    }

    // Son 15 dk içindeki 5 dakikalık periyot verileri
    @GetMapping("/recent-prices")
    public ResponseEntity<List<CryptoPriceDTO>> getRecentPrices(@RequestParam String symbol) {
        List<CryptoPriceDTO> prices = cryptoService.getRecentPrices(symbol);
        return ResponseEntity.ok(prices);
    }

    // Manuel veri çekme ve veritabanına kaydetme
    @PostMapping("/fetch-now")
    public ResponseEntity<CryptoPriceDTO> fetchAndSaveCryptoPrice(@RequestParam String symbol) {
        CryptoPriceDTO savedPrice = cryptoService.fetchAndSaveCryptoPrice(symbol);
        return ResponseEntity.ok(savedPrice);
    }
}
