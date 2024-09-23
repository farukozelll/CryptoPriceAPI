package com.backend.sade.controller;

import com.backend.sade.dto.ExchangeRateDTO;
import com.backend.sade.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExchangeRateDTO>> getAllExchangeRates() {
        List<ExchangeRateDTO> allRates = exchangeRateService.getAllExchangeRates();
        return ResponseEntity.ok(allRates);
    }

    @GetMapping("/fetch-now")
    public ResponseEntity<ExchangeRateDTO> fetchAndSaveExchangeRateNow() {
        ExchangeRateDTO savedRate = exchangeRateService.fetchAndSaveExchangeRateNow();
        return ResponseEntity.ok(savedRate);
    }
}
