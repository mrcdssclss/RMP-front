# WORKLOG_DEV2

## 1. Что было найдено в проекте
- Android-проект Kotlin + Jetpack Compose.
- В исходниках найдены только `MainActivity.kt` и `ui/theme/*`; экранов, навигации, компонентов, ViewModel, State, repositories и data/domain слоя не было.
- PDF-документы `РМП интерфейс.pdf`, `РМП 2026 весна Лекция 4 Архитектура.pdf`, `РМП 2026 весна Лекция 1. Практическое задание.pdf` в дереве `/Users/polina/itmo/tpo/RMP-front` не найдены.
- `package-lock.json` уже был неотслеживаемым файлом и не относится к Android-сборке.

## 2. Read-only зоны UI Разработчика 1
- `app/src/main/java/com/example/rmp_frontend/ui/theme/*`
- `app/src/main/java/com/example/rmp_frontend/MainActivity.kt`
- Потенциальные будущие UI-зоны: `presentation/screens/`, `ui/screens/`, `presentation/components/`, `ui/components/`, `presentation/navigation/`, `ui/navigation/`, `presentation/theme/`, `ui/theme/`

Эти зоны не переписывались.

## 3. Какие файлы были созданы/изменены Разработчиком 2
- Созданы `domain/model/*`: `User`, `AuthToken`, `Instrument`, `PricePoint`, `ChartPeriod`, `Portfolio`, `PortfolioItem`, `Transaction`, `TransactionType`, `TradeResult`.
- Созданы `domain/repository/*`: `AuthRepository`, `MarketRepository`, `InstrumentRepository`, `TradingRepository`, `PortfolioRepository`, `HistoryRepository`, `ProfileRepository`.
- Созданы `data/api/*`: `ApiClient`, `AuthApi`, `MarketApi`, `InstrumentApi`, `TradingApi`, `PortfolioApi`, `HistoryApi`, `ProfileApi`.
- Созданы `data/dto/*`, `data/mapper/*`, `data/repository/*`, `data/storage/TokenStorage.kt`, `data/websocket/PriceWebSocketClient.kt`, `data/mock/*`.
- Созданы `presentation/state/*`, `presentation/viewmodel/*`, `presentation/di/AppContainer.kt`, `presentation/di/ViewModelFactory.kt`.
- Изменены `gradle/libs.versions.toml` и `app/build.gradle.kts`: добавлены lifecycle-viewmodel, coroutines, Retrofit, Gson converter, OkHttp, DataStore Preferences.
- Изменен `app/src/main/AndroidManifest.xml`: добавлен `android.permission.INTERNET` для REST/WebSocket.

## 4. Какие ViewModel реализованы
- `AuthViewModel`: `checkSession()`, `login()`, `register()`, `logout()`.
- `MarketViewModel`: `loadInstruments()`, `refresh()`, `subscribeToPriceUpdates()`, `unsubscribeFromPriceUpdates()`.
- `InstrumentViewModel`: `loadInstrument()`, `loadPriceHistory()`, `changePeriod()`, `buyInstrument()`, `sellInstrument()`.
- `PortfolioViewModel`: `loadPortfolio()`, `refresh()`.
- `HistoryViewModel`: `loadTransactions()`, `refresh()`.
- `ProfileViewModel`: `loadProfile()`, `logout()`.

## 5. Какие repository реализованы
- Interfaces: `AuthRepository`, `MarketRepository`, `InstrumentRepository`, `TradingRepository`, `PortfolioRepository`, `HistoryRepository`, `ProfileRepository`.
- Real implementations: `AuthRepositoryImpl`, `MarketRepositoryImpl`, `InstrumentRepositoryImpl`, `TradingRepositoryImpl`, `PortfolioRepositoryImpl`, `HistoryRepositoryImpl`, `ProfileRepositoryImpl`.
- Mock implementations: `MockAuthRepository`, `MockMarketRepository`, `MockInstrumentRepository`, `MockTradingRepository`, `MockPortfolioRepository`, `MockHistoryRepository`, `MockProfileRepository`.

## 6. Какие mock-данные добавлены
- `MockData` содержит инструменты `SBER`, `GAZP`, `LKOH`, `YNDX`.
- Добавлены demo profile, portfolio, transactions.
- `MockMarketRepository` генерирует обновления котировок через `Flow`.
- `MockTradingRepository` возвращает успешный `TradeResult` для buy/sell и валидирует quantity.

## 7. Как переключиться с mock на real backend
- Сейчас `AppContainer` по умолчанию использует `useMock = true`.
- Для real backend нужно создать `AppContainer(context, useMock = false, baseUrl = "...", webSocketUrl = "...")`.
- UI должен получать ViewModel через `ViewModelFactory(AppContainer(...))`, не создавая repository/API напрямую.

## 8. Какие endpoint'ы ожидаются от backend
- `POST /auth/login`
- `POST /auth/register`
- `GET /instruments`
- `GET /instruments/{id}`
- `GET /instruments/{id}/history?period=...`
- `POST /orders/buy`
- `POST /orders/sell`
- `GET /portfolio`
- `GET /transactions`
- `GET /profile`
- WebSocket для котировок ожидает сообщения в формате `InstrumentDto`.

## 9. Как работает хранение токена
- `TokenStorage` реализован через DataStore Preferences.
- После `login/register` repository сохраняет `AuthToken`.
- `checkSession()` читает токен через repository.
- `logout()` очищает токен.
- `ApiClient` добавляет `Authorization: Bearer <token>` через OkHttp interceptor.

## 10. Как работает WebSocket
- `PriceWebSocketClient` использует OkHttp WebSocket.
- При подключении добавляет Authorization header, если токен есть.
- Сообщения парсятся в `InstrumentDto`, маппятся в domain `Instrument` и отдаются через `SharedFlow`.
- `MarketViewModel` подписывается на Flow и обновляет список инструментов через upsert.

## 11. Какие команды сборки были запущены
- `git status --short`
- `./gradlew :app:compileDebugKotlin` - первый запуск упал из-за временной сетевой ошибки `dl.google.com`, повторный запуск успешен.
- `./gradlew :app:assembleDebug` - успешно.
- `./gradlew build` - успешно.

## 12. Какие ошибки были исправлены
- Настроены зависимости, которых не хватало для ViewModel, coroutines, DataStore, Retrofit и OkHttp.
- Добавлен `INTERNET` permission для REST/WebSocket.
- Первый сбой Gradle был сетевым: `dl.google.com` / TLS. Повторный запуск сборки успешно скачал зависимости.

## 13. Какие ограничения остались
- PDF-документы не найдены локально, реализация выполняется по архитектурному описанию из задачи.
- Backend URL и WebSocket URL пока placeholder: `https://example.com/api/` и `wss://example.com/quotes`.
- Real DTO могут потребовать точной подгонки, когда backend зафиксирует реальные JSON-поля.
- `package-lock.json` остается неотслеживаемым файлом, он был найден до начала реализации и не изменялся как часть Android-логики.
