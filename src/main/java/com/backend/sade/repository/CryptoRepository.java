package com.backend.sade.repository;

import com.backend.sade.entity.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CryptoRepository extends JpaRepository<CryptoPrice, Long> {

    // Belirtilen simge ve zaman aralığına göre fiyatları getirir
    List<CryptoPrice> findBySymbolAndTimestampBetween(String symbol, LocalDateTime start, LocalDateTime end);

    // Son 60 dakika için ortalama fiyatı hesaplar
    @Query("SELECT AVG(c.priceBtc) FROM CryptoPrice c WHERE c.symbol = :symbol AND c.timestamp >= :timestamp")
    BigDecimal findAveragePrice(String symbol, LocalDateTime timestamp);
}