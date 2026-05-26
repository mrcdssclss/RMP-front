# Codex Worklog: API Integration

## Что найдено в backend

- Фактический backend находится в
  `/Users/lepidodendronnnn/IdeaProjects/rmp-api-service/rmp-api-service`.
- Это Spring Boot/Kotlin сервис с Maven wrapper (`./mvnw`), а не Ktor/Gradle.
- REST port: `8080`; market WebSocket: `/ws/market`.
- Защита: JWT Bearer; без токена доступны только auth/служебные маршруты.
- Зависимости локального запуска: PostgreSQL `localhost:5432/rmp` и Redis
  `localhost:6379`.
- В исходной конфигурации стоит `spring.jpa.hibernate.ddl-auto: validate`;
  миграции для чистой локальной базы в проекте не найдены.

## Подключенные endpoint'ы

| Возможность | Backend endpoint | Frontend слой |
| --- | --- | --- |
| Register | `POST /api/auth/register` | `AuthViewModel -> AuthRepositoryImpl -> AuthApi` |
| Login | `POST /api/auth/login` | `AuthViewModel -> AuthRepositoryImpl -> AuthApi` |
| Market / refresh | `GET /api/market/instruments` | `MarketViewModel -> MarketRepositoryImpl -> MarketApi` |
| Live market price | `WS /ws/market` | `MarketViewModel -> MarketRepositoryImpl -> PriceWebSocketClient` |
| Instrument details | `GET /api/market/instruments` | `InstrumentViewModel` resolves selected ticker from real list |
| Chart periods | `GET /api/market/prices/history` | `InstrumentViewModel -> InstrumentRepositoryImpl -> InstrumentApi` |
| Buy / Sell | `POST /api/trades/buy`, `POST /api/trades/sell` | confirmation dialog -> `InstrumentViewModel -> TradingRepositoryImpl` |
| History | `GET /api/trades/history` | `HistoryViewModel -> HistoryRepositoryImpl -> HistoryApi` |
| Profile | `GET /api/profile` | `ProfileViewModel -> ProfileRepositoryImpl -> ProfileApi` |
| Display name settings | `PATCH /api/profile` | `ProfileViewModel -> ProfileRepositoryImpl -> ProfileApi` |

JWT из register/login сохраняется через существующий `TokenStorage`.
`ApiClient` и `PriceWebSocketClient` добавляют `Authorization: Bearer <token>`.

## Отсутствующие endpoints и fallback

- Реальный Portfolio positions REST endpoint отсутствует. `PortfolioViewModel`
  пока использует fallback `MockPortfolioRepository`, который строит mock-позиции
  только по инструментам из `MarketRepository` (`GET /api/market/instruments`).
  При текущем backend это одна позиция `AAPL`; клик по ней открывает реальный
  `InstrumentScreen` для `AAPL`.
- Endpoint изменения email/password отсутствует. Settings изменяет только имя
  через поддерживаемый `PATCH /api/profile`; fake password mutation удалена.
- Отдельного endpoint деталей инструмента нет. Детали формируются из записи
  `/api/market/instruments`.
- Backend не возвращает дневной процент изменения инструмента; UI отображает
  `0.0%` до появления такого поля/endpoint.

## Измененные frontend файлы

- Конфигурация и transport:
  `AndroidManifest.xml`, `MainActivity.kt`,
  `data/api/ApiConfig.kt`, `data/api/ApiClient.kt`, API interfaces,
  DTO, mappers, `data/websocket/PriceWebSocketClient.kt`.
- Domain/data:
  repository contracts, real repository implementations и согласованные mock
  implementations для fallback.
- Presentation:
  `presentation/di/AppContainer.kt`, `ViewModelFactory.kt`,
  `navigation/AppNavGraph.kt`, UI state и ViewModel для auth/splash/market/
  instrument/portfolio/history/profile, `InstrumentScreen.kt`,
  `ProfileScreen.kt`.
- Документация: `API_INTEGRATION_MAP.md`, `WORKLOG_CODEX.md`.

## Измененные backend файлы

- Backend source files не изменялись.
- Для локальной проверки были запущены контейнеры `rmp-codex-postgres` и
  `rmp-codex-redis`; в локальную БД добавлен тестовый инструмент `AAPL`,
  а в Redis установлена текущая тестовая цена.

## Как запустить backend локально

Первый запуск зависимостей:

```bash
docker run --name rmp-codex-postgres -e POSTGRES_DB=rmp -e POSTGRES_USER=rmp -e POSTGRES_PASSWORD=rmp -p 5432:5432 -d postgres:16-alpine
docker run --name rmp-codex-redis -p 6379:6379 -d redis:7-alpine
```

При повторном запуске:

```bash
docker start rmp-codex-postgres rmp-codex-redis
```

Запуск API на чистой локальной БД:

```bash
cd /Users/lepidodendronnnn/IdeaProjects/rmp-api-service/rmp-api-service
SPRING_JPA_HIBERNATE_DDL_AUTO=update ./mvnw spring-boot:run
```

Override `ddl-auto=update` нужен только потому, что сервис не содержит
миграций для создания пустой локальной схемы.

## Как собрать и запустить frontend

```bash
cd /Users/lepidodendronnnn/IdeaProjects/RMPfrontend
./gradlew :app:compileDebugKotlin
./gradlew :app:assembleDebug
```

В Android emulator приложение использует единый конфиг:
REST `http://10.0.2.2:8080/api/`, WebSocket
`ws://10.0.2.2:8080/ws/market`.

## Как протестировать сценарии

1. Register/Login: зарегистрировать пользователя и войти; после auth
   защищенные экраны должны перестать возвращать `401`.
2. Market: открыть экран и выполнить refresh; список приходит из backend.
   WebSocket подключается через repository, а не из Compose.
3. Instrument: открыть `AAPL` и переключить `1D/1W/1M/1Y/ALL`; каждый период
   загружает историю через repository/API и обновляет график.
4. Buy/Sell: выбрать количество, подтвердить dialog; UI показывает
   loading/success/error, а операция появляется в History.
5. Portfolio: экран остается документированным mock fallback, синхронизированным
   с backend market instruments; клик по `AAPL` ведет на подключенный Instrument.
6. History: refresh читает `GET /api/trades/history`.
7. Profile/Settings: профиль читается из API; изменение display name вызывает
   `PATCH /api/profile`. Изменение email/password не поддерживается backend.

## Выполненная проверка

- Backend стартовал на `8080`, health вернул `UP`.
- `curl` успешно проверил register/login, market instruments, price history,
  profile GET/PATCH, buy, sell и trade history.
- Для trade test использовались `AAPL` по цене `189.45`, затем в истории
  появились операции BUY и SELL.
- `./gradlew :app:compileDebugKotlin` и `./gradlew :app:assembleDebug`
  завершились успешно.
- `git diff --check` не обнаружил whitespace-ошибок; audit presentation-кода
  не обнаружил пустых `onClick = {}` или `TODO()` обработчиков.

## Оставшиеся ограничения

- Для полноценного Portfolio нужен новый backend controller/service contract.
- Для production нужны миграции БД вместо локального `ddl-auto=update`.
- Процент изменения цены и изменение credentials потребуют дополнительных
  backend контрактов.
- При локальном DEBUG-запуске backend логирует request body регистрации,
  включая пароль; перед реальным использованием этот уровень/логирование
  следует отключить или маскировать.
