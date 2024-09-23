package com.backend.sade.service;

import com.backend.sade.dto.CryptoPriceDTO;
import com.backend.sade.entity.CryptoPrice;
import com.backend.sade.repository.CryptoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CryptoService {
    /**
     * Bu sınıf, kripto para fiyat verilerini yönetir.
     * Binance API'den fiyat verilerini çekerek veritabanına kaydeder.
     * Belirli zaman dilimlerine ait fiyat verilerini sağlar.
     */
    private final CryptoRepository cryptoRepository;
    private final RestTemplate restTemplate;

    @Value("${binance.api.url}")
    private String binanceApiUrl;

    public CryptoService(CryptoRepository cryptoRepository, RestTemplate restTemplate) {
        this.cryptoRepository = cryptoRepository;
        this.restTemplate = restTemplate;
    }

    // 5 dakikada bir Binance API'den kripto para fiyatlarını al ve veritabanına kaydet
    @Scheduled(fixedRate = 300000) // 5 dakika (300000 ms)
    public void fetchAndSaveScheduledCryptoPrices() {
        List<String> symbols = List.of("BNBBTC", "ETHBTC", "XRPBTC", "BCHBTC", "LTCBTC");
        symbols.forEach(this::fetchAndSaveCryptoPrice);
    }

    // Verilen sembol için anlık fiyatı çek ve kaydet
    public CryptoPriceDTO fetchAndSaveCryptoPrice(String symbol) {
        String url = binanceApiUrl + "/api/v3/ticker/price?symbol=" + symbol;
        Map<String, String> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.get("price") != null) {
            BigDecimal priceBtc = new BigDecimal(response.get("price"));

            // USDT cinsinden fiyatı almak için ayrı bir API çağrısı yapılabilir.
            String usdtSymbol = symbol.replace("BTC", "USDT");
            String usdtUrl = binanceApiUrl + "/api/v3/ticker/price?symbol=" + usdtSymbol;
            Map<String, String> usdtResponse = restTemplate.getForObject(usdtUrl, Map.class);

            if (usdtResponse != null && usdtResponse.get("price") != null) {
                BigDecimal priceUsdt = new BigDecimal(usdtResponse.get("price"));

                CryptoPrice cryptoPrice = CryptoPrice.builder()
                        .symbol(symbol)
                        .priceBtc(priceBtc)
                        .priceUsdt(priceUsdt)
                        .timestamp(LocalDateTime.now())
                        .build();

                // Veritabanına kaydet
                cryptoRepository.save(cryptoPrice);
                log.info("Kripto fiyatı kaydedildi: {}", cryptoPrice);

                // DTO olarak geri dön
                return CryptoPriceDTO.builder()
                        .symbol(cryptoPrice.getSymbol())
                        .priceBtc(cryptoPrice.getPriceBtc())
                        .priceUsdt(cryptoPrice.getPriceUsdt())
                        .timestamp(cryptoPrice.getTimestamp())
                        .build();
            } else {
                log.error("USDT fiyatı alınamadı. Yanıt: {}", usdtResponse);
                throw new RuntimeException("USDT fiyatı alınamadı");
            }
        } else {
            log.error("BTC fiyatı alınamadı. Yanıt: {}", response);
            throw new RuntimeException("BTC fiyatı alınamadı");
        }
    }

    // Son 60 dakika içerisindeki ortalama fiyatı döner
    public BigDecimal getHourlyAverage(String symbol) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return cryptoRepository.findAveragePrice(symbol, oneHourAgo);
    }

    // Son 15 dakikadaki 5 dakikalık periyot verilerini döner
    public List<CryptoPriceDTO> getRecentPrices(String symbol) {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);
        List<CryptoPrice> prices = cryptoRepository.findBySymbolAndTimestampBetween(symbol, fifteenMinutesAgo, LocalDateTime.now());

        return prices.stream()
                .map(price -> CryptoPriceDTO.builder()
                        .symbol(price.getSymbol())
                        .priceBtc(price.getPriceBtc())
                        .priceUsdt(price.getPriceUsdt())
                        .timestamp(price.getTimestamp())
                        .build())
                .collect(Collectors.toList());
    }
}
