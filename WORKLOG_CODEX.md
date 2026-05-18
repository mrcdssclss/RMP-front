# Codex Worklog

## Initial findings

- Android project uses Gradle Kotlin DSL: `settings.gradle.kts`, root `build.gradle.kts`, `app/build.gradle.kts`.
- Compose and Navigation Compose are already connected in the app module.
- Current entry point is `MainActivity`, which opens a minimal Compose navigation graph.
- Existing UI is a small prototype: login, home, portfolio/profile placeholders, bottom bar, and a `HomeViewModel`.
- Existing data/model files are limited to a local `AssetRepository` stub and `Asset` model.
- Attached PDFs could not be extracted as plain text locally because at least the interface PDF is rendered as PDF/image content without a useful text layer. The implementation follows the architecture and UI requirements explicitly listed in the task.

## Short plan

- Keep Data Layer untouched.
- Replace prototype UI navigation with a Presentation Layer implementation based on MVVM.
- Add explicit UI states for Loading, Success, Error, Empty where applicable.
- Add ViewModel stubs that expose UI State and receive UI events without backend/API/token logic.
- Implement Splash, Auth/Login/Register, Market, Instrument, Portfolio, History, Profile screens.
- Update docs and verify compilation.

## Completed changes

- Added Presentation Layer package: `presentation/navigation`, `presentation/screens`, `presentation/components`, `presentation/state`, `presentation/viewmodel`.
- Implemented screens:
  - SplashScreen
  - AuthScreen
  - LoginScreen
  - RegisterScreen
  - MarketScreen
  - InstrumentScreen
  - PortfolioScreen
  - HistoryScreen
  - ProfileScreen
- Implemented navigation:
  - Splash to Auth/Main by navigation state
  - Auth to Market after successful UI submit
  - Bottom navigation for Market, Portfolio, History, Profile
  - Market to Instrument by ticker
  - Profile logout to Auth
- Added reusable UI components:
  - AppButton
  - AppTextField
  - LoadingView
  - ErrorView
  - EmptyView
  - InstrumentCard
  - PortfolioItemCard
  - TransactionItem
  - PriceChangeBadge
  - AppTopBar
- Added explicit UI state classes for Auth, Market, Instrument, Portfolio, History, Profile, Splash.
- Updated `MainActivity` to open the new `AppNavGraph`.
- Updated the Compose theme to a dark investment-terminal style.
- Removed old prototype UI/navigation/ViewModel files that duplicated the new Presentation Layer.
- Added `docs/frontend-ui.md`.

## Architecture decisions

- Data Layer was not modified except leaving existing local data stubs in place.
- Composable functions do not call backend, API clients, repositories, WebSocket, or token storage.
- User actions are sent to ViewModel methods or navigation callbacks.
- ViewModels expose `StateFlow` UI state and currently use in-memory sample UI data only as compile-safe placeholders.
- Buy/sell buttons only report that a request was sent to the ViewModel; no business purchase/sale logic is implemented in UI.

## Remaining limitations

- Splash always routes to Auth because real authorization/token state belongs to Developer 2.
- Market, portfolio, history, profile, and instrument details use local sample UI data.
- Auth success is a UI-level stub and does not perform real authentication.
- There is no real API client, REST/WebSocket integration, token persistence, or backend error mapping in this work.

## Developer 2 follow-up

- Implement backend API client and repositories in the Data Layer.
- Connect ViewModels to repositories through the agreed architecture.
- Add token Local Storage and expose authorization state to Splash/Auth/Profile.
- Implement real buy/sell business flow outside the UI layer.
- Add WebSocket/streaming market updates and map them into UI State.
- Replace sample UI data with real domain/data models.

## Verification

- `./gradlew :app:compileDebugKotlin` completed successfully.
- `./gradlew :app:assembleDebug` completed successfully.
