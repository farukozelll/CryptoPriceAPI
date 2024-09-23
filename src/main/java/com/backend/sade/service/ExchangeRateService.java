package com.backend.sade.service;

import com.backend.sade.dto.ExchangeRateDTO;
import com.backend.sade.entity.ExchangeRate;
import com.backend.sade.repository.ExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExchangeRateService {

    private final RestTemplate restTemplate;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ModelMapper modelMapper;

    @Value("${fixer.api.url}")
    private String fixerApiUrl;

    @Value("${fixer.api.key}")
    private String fixerApiKey;

    public ExchangeRateService(RestTemplate restTemplate, ExchangeRateRepository exchangeRateRepository, ModelMapper modelMapper) {
        this.restTemplate = restTemplate;
        this.exchangeRateRepository = exchangeRateRepository;
        this.modelMapper = modelMapper;
    }

    // Anlık veri çekme ve veritabanına kaydetme metodu
    public ExchangeRateDTO fetchAndSaveExchangeRateNow() {
        String url = fixerApiUrl + "?access_key=" + fixerApiKey + "&symbols=USD,TRY";
        Map<String, Object> response = null;
        try {
            response = restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            log.error("API çağrısı sırasında bir hata oluştu: {}", e.getMessage());
            throw new RuntimeException("API çağrısı sırasında bir hata oluştu: " + e.getMessage());
        }

        if (response != null && (Boolean) response.get("success")) {
            // Veriyi alırken tür dönüşümünü doğrudan `Double` olarak yapıyoruz
            Map<String, Double> rates = (Map<String, Double>) response.get("rates");

            // `Double` türünden gelen veriyi `BigDecimal`'e çeviriyoruz
            BigDecimal usdRate = BigDecimal.valueOf(rates.get("USD"));
            BigDecimal tryRate = BigDecimal.valueOf(rates.get("TRY"));

            if (usdRate != null && tryRate != null) {
                // USD/TRY oranını hesaplıyoruz: 1 USD = TRY/USD
                BigDecimal usdTryRate = tryRate.divide(usdRate, 4, RoundingMode.HALF_UP);

                ExchangeRate exchangeRate = ExchangeRate.builder()
                        .currencyPair("USD/TRY")
                        .rate(usdTryRate)
                        .timestamp(LocalDateTime.now())
                        .build();

                exchangeRateRepository.save(exchangeRate);
                log.info("Döviz kuru kaydedildi: {}", exchangeRate);

                // DTO olarak geri dön
                return modelMapper.map(exchangeRate, ExchangeRateDTO.class);
            } else {
                log.error("USD veya TRY kuru alınamadı.");
                throw new RuntimeException("USD veya TRY kuru alınamadı.");
            }
        } else {
            log.error("API'den veri alınamadı veya istek başarısız oldu. Yanıt: {}", response);
            throw new RuntimeException("API'den veri alınamadı veya istek başarısız oldu.");
        }
    }
    public List<ExchangeRateDTO> getAllExchangeRates() {
        List<ExchangeRate> allRates = exchangeRateRepository.findAll();
        return allRates.stream()
                .map(rate -> modelMapper.map(rate, ExchangeRateDTO.class))
                .collect(Collectors.toList());
    }




}
