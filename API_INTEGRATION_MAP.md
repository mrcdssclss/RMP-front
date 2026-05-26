# API Integration Map

Backend в соседнем проекте фактически реализован на Spring Boot, а не на Ktor. Default port из
`application.yml`: `8080`. Android emulator обращается к REST через
`http://10.0.2.2:8080/api/`, к market WebSocket через `ws://10.0.2.2:8080/ws/market`.

Все маршруты кроме `/api/auth/*` требуют `Authorization: Bearer <token>`. Frontend сохраняет
JWT в `TokenStorage` и добавляет header в `ApiClient`/`PriceWebSocketClient`.

| Endpoint | Method | Request | Response | Frontend screen | ViewModel | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `/api/auth/login` | POST | `{email, password}` | `{token}` | Login | `AuthViewModel` | connected |
| `/api/auth/register` | POST | `{email, password, firstName?, lastName?}` | `{token}` | Register | `AuthViewModel` | connected |
| `/api/market/instruments` | GET | - | `[{ticker, name, currency, latestPrice}]` | Market | `MarketViewModel` | connected |
| `/ws/market` | WebSocket | Bearer handshake | `{ticker, price, ...}` updates | Market | `MarketViewModel` | connected |
| `/api/market/instruments` | GET | - | selected ticker resolved from list | Instrument | `InstrumentViewModel` | connected (derived detail) |
| `/api/market/prices/history` | GET | `ticker`, `from`, `to`, `limit` | `[{ticker, price, at}]` | Instrument chart/periods | `InstrumentViewModel` | connected |
| `/api/trades/buy` | POST | `{ticker, quantity}` | `{transactionId, side, ticker, price, quantity, total, createdAt}` | Instrument confirmation dialog | `InstrumentViewModel` | connected |
| `/api/trades/sell` | POST | `{ticker, quantity}` | `{transactionId, side, ticker, price, quantity, total, createdAt}` | Instrument confirmation dialog | `InstrumentViewModel` | connected |
| `/api/trades/history` | GET | optional backend `limit` | list of trade results | History | `HistoryViewModel` | connected |
| `/api/profile` | GET | - | `{userId, email, firstName, lastName, balanceCurrency, balanceAmount}` | Profile | `ProfileViewModel` | connected |
| `/api/profile` | PATCH | `{firstName?, lastName?}` | empty body | Profile settings: display name | `ProfileViewModel` | connected |
| portfolio positions endpoint | - | - | - | Portfolio | `PortfolioViewModel` | missing / mock fallback |
| email/password credentials update endpoint | - | - | - | Settings | `ProfileViewModel` | missing / no mutation fallback |
| daily price change field | - | - | backend instrument response has no change percent | Market/Instrument | `MarketViewModel`, `InstrumentViewModel` | missing / displays `0.0%` fallback |

## Notes

- Portfolio navigation remains active: mock positions open `InstrumentScreen`, which then loads
  real market/instrument data for matching tickers when the backend contains that ticker.
- History is reloaded from backend when its tab is opened or refreshed after trading.
- A legacy unused `PortfolioApi`/`PortfolioRepositoryImpl` exists in the imported logic layer,
  but `AppContainer` deliberately wires `MockPortfolioRepository` because the backend exposes no
  portfolio REST controller.
- Local runtime verification used PostgreSQL and Redis containers plus
  `SPRING_JPA_HIBERNATE_DDL_AUTO=update ./mvnw spring-boot:run`, because the backend repository
  has `ddl-auto: validate` but no migrations for an empty database.
- Verified with `curl`: health, register/login, market instruments, price history, profile
  GET/PATCH, buy/sell and trades history all returned successful responses.
