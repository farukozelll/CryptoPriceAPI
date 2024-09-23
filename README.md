
# Backend Teknik Ödev

## Proje Özeti
Bu proje, Binance API'lerinden BNB, ETH, XRP, BCH ve LTC gibi kripto paraların Bitcoin ve USDT cinsinden fiyatlarını çekerek PostgreSQL veritabanına kaydeder ve bu verileri başka bir API üzerinden sunar. 5 dakikalık periyotlarla veri alınarak saklanır ve bu veriler üzerinden çeşitli analiz ve sorgular yapılabilir. Örneğin, son 15 dakikadaki 5 dakikalık veriler veya son 60 dakikalık ortalama değerler API aracılığıyla sunulur.

## Proje Yapısı ve Teknolojiler
- **Backend:** Spring Boot, Spring Security, Hibernate, Spring Data JPA
- **Veritabanı:** PostgreSQL
- **API İstemcisi:** RestTemplate, Binance API
- **JWT:** Kimlik doğrulama ve yetkilendirme için JWT kullanımı.
- **Dokümantasyon:** Swagger, OpenAPI
- **Docker:** Docker ve Docker Compose ile konteynerleşme.
- **Diğer Araçlar:** Lombok, ModelMapper

## Klasör Yapısı
```plaintext
project-root
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── backend
│   │   │           ├── config
│   │   │           │   ├── SecurityConfig.java        // Spring Security yapılandırması
│   │   │           │   ├── SwaggerConfig.java         // Swagger dokümantasyon ayarları
│   │   │           │   └── WebConfig.java             // CORS ve genel web yapılandırmaları
│   │   │           ├── controller
│   │   │           │   ├── ExchangeRateController.java // Döviz kuru verilerini işleyen controller
│   │   │           │   ├── CryptoPriceController.java  // Kripto fiyat verilerini işleyen controller
│   │   │           │   ├── UserController.java         // Kullanıcı işlemleri için
│   │   │           │   └── AuthController.java         // Kimlik doğrulama işlemleri için
│   │   │           ├── dto
│   │   │           │   ├── ExchangeRateDTO.java        // Döviz kuru verilerini taşıyan DTO
│   │   │           │   ├── CryptoPriceDTO.java         // Kripto fiyat verilerini taşıyan DTO
│   │   │           │   ├── UserDTO.java                // Kullanıcı verilerini taşıyan DTO
│   │   │           │   ├── LoginRequest.java           // Giriş işlemleri için DTO
│   │   │           │   ├── RegisterRequest.java        // Kayıt işlemleri için DTO
│   │   │           │   └── TokenResponse.java          // JWT Token yanıt DTO'su
│   │   │           ├── entity
│   │   │           │   ├── ExchangeRate.java           // Döviz kuru varlık modeli (Entity)
│   │   │           │   ├── CryptoPrice.java            // Kripto fiyat varlık modeli (Entity)
│   │   │           │   └── User.java                   // Kullanıcı varlık modeli (Entity)
│   │   │           ├── exception                       // Özel istisnalar ve global exception handling
│   │   │           │   ├── GlobalExceptionHandler.java // Global exception handler
│   │   │           ├── repository
│   │   │           │   ├── ExchangeRateRepository.java // Döviz kuru veritabanı işlemleri
│   │   │           │   ├── CryptoPriceRepository.java  // Kripto fiyat veritabanı işlemleri
│   │   │           │   └── UserRepository.java         // Kullanıcı veritabanı işlemleri
│   │   │           ├── security
│   │   │           │   ├── CustomUserDetailsService.java // Kullanıcı detaylarını yükleyen servis
│   │   │           │   ├── JwtAuthenticationFilter.java // JWT doğrulama filtresi
│   │   │           │   ├── JwtTokenProvider.java        // JWT token üretme ve doğrulama
│   │   │           ├── service
│   │   │           │   ├── ExchangeRateService.java     // Döviz kuru işlemleri servisi
│   │   │           │   ├── CryptoPriceService.java      // Kripto fiyat işlemleri servisi
│   │   │           │   ├── UserService.java             // Kullanıcı işlemleri servisi
│   │   │           │   └── AuthService.java             // Kimlik doğrulama işlemleri servisi
│   │   └── resources
│   │       ├── application.yml                          // Genel yapılandırmalar
│   │       └── application-dev.yml                      // Geliştirme ortamı için yapılandırmalar
└── docker-compose.yml                                   // Docker Compose dosyası
```

## Gereksinimler
- **Java 11** veya üstü
- **Maven 3.6** veya üstü
- **PostgreSQL 12** veya üstü
- **Docker ve Docker Compose**

## Kurulum ve Çalıştırma

### 1. Projeyi Klonlayın
```bash
git clone https://github.com/your-username/your-repository.git
cd your-repository
```

### 2. Docker Kullanarak Projeyi Çalıştırın
Projeyi Docker ile çalıştırmak için aşağıdaki adımları izleyin:

- `docker-compose.yml` dosyasında yer alan PostgreSQL servis yapılandırmalarını kontrol edin.
- Aşağıdaki komutu kullanarak Docker ile tüm servisleri başlatın:
  ```bash
  docker-compose up --build
  ```

- Servisler çalıştığında, backend servisi `http://localhost:8080` adresinde, veritabanı ise `localhost:5432` portunda çalışacaktır.

### 3. Çevresel Değişkenleri Ayarlayın
`application.yml` dosyasındaki PostgreSQL ve Binance API bilgilerini güncelleyin:

- **PostgreSQL** bağlantı bilgileri (`application.yml`):
  ```yaml
  spring:
    datasource:
      url: jdbc:postgresql://localhost:5432/your_database_name
      username: your_database_username
      password: your_database_password
  ```

- **Binance API** URL'si ve simgeleri (`application.yml`):
  ```yaml
  binance:
    api:
      url: https://api.binance.com
      symbols:
        - BNBBTC
        - ETHBTC
        - XRPBTC
        - BCHBTC
        - LTCBTC
        - BTCUSDT
  ```

### 4. Backend Uygulamasını Manuel Olarak Çalıştırın
```bash
# Backend dizinine geçin
cd backend

# Maven kullanarak projeyi temizleyip derleyin
mvn clean install

# Uygulamayı çalıştırın
mvn spring-boot:run
```

### 5. Swagger Dökümantasyonu
Swagger ile API dökümantasyonuna erişmek için aşağıdaki URL'yi kullanabilirsiniz:
```
http://localhost:8080/public/docs
```

### 6. API Testi ve Örnek İstekler

#### Son 15 Dakikadaki 5 Dakikalık ETH/BTC Değerleri
```http
GET /api/crypto/recent-prices?symbol=ETHBTC
```

#### Son 60 Dakikadaki ETH/BTC Ortalama Değeri
```http
GET /api/crypto/hourly-average?symbol=ETHBTC
```

## Testler

### Backend Testleri
```bash
# Tüm backend testlerini çalıştırın
mvn test
```


## Hata Giderme
- **Veritabanı Bağlantı Hatası:** PostgreSQL servisinin çalıştığından ve `application.yml` dosyasındaki veritabanı bağlantı bilgilerinin doğru olduğundan emin olun.
- **Port Çakışması:** Eğer `8080` veya `5432` portları başka servisler tarafından kullanılıyorsa, `application.yml` ve `docker-compose.yml` dosyalarında bu portları değiştirin.

## Geliştirici Notları
- Proje, yerel geliştirme ortamında Docker kullanılarak kolayca başlatılabilir.
- API dökümantasyonu ve test senaryoları eklenmiştir.
- Kullanıcı yetkilendirme ve kimlik doğrulama işlemleri JWT ile güvence altına alınmıştır.


## Lisans
Bu proje MIT Lisansı ile lisanslanmıştır - detaylar için [LICENSE.md](LICENSE.md) dosyasına bakın.
