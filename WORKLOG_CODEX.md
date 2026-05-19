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

## Interactivity pass

- Previously weak/decorative actions:
  - `Refresh` in top bars and empty/error retry views called ViewModel methods, but did not give visible immediate feedback when sample data reloaded synchronously.
  - `Buy` and `Sell` on `InstrumentScreen` called ViewModel methods directly without confirmation, quantity handoff in the callback contract, or snackbar feedback.
  - `Success` states with empty lists on Market/Portfolio/History could render an empty list instead of the explicit empty state.
- Added callbacks/wiring:
  - `InstrumentScreen.onBuyClick(quantity: Double)`
  - `InstrumentScreen.onSellClick(quantity: Double)`
  - `InstrumentViewModel.onBuyClick(quantity: Double)`
  - `InstrumentViewModel.onSellClick(quantity: Double)`
  - Refresh actions on Market, Instrument, Portfolio, History, and Profile now call the existing refresh callback and show snackbar feedback: `Данные обновляются`.
- UI feedback added:
  - Buy/Sell now open a confirmation dialog with ticker, price, quantity, and total amount.
  - Confirming Buy/Sell calls the corresponding callback/ViewModel method and shows:
    - `Заявка на покупку отправлена`
    - `Заявка на продажу отправлена`
  - Invalid Buy/Sell quantity still reaches the callback as a UI-level validation stub and shows `Введите количество`.
  - Operation success/error messages from `InstrumentUiState.Success` remain visible in the screen content.
  - Empty list `Success` states now render `EmptyView` with retry instead of a blank list.
- UI stubs that remain by design:
  - Refresh reloads in-memory sample UI data only.
  - Buy/Sell only update UI state and snackbar messages; no order is sent.
  - Auth/profile/portfolio/history/market sample data is still local Presentation Layer placeholder data.
- Developer 2 follow-up:
  - Connect refresh methods to repository/use-case loading state and backend error mapping.
  - Replace Buy/Sell UI stub with a real order submission flow outside Composables.
  - Preserve the `quantity` callback contract when wiring domain/order use cases.
  - Map backend success/error messages into `UiState` so the Presentation Layer can keep displaying them.
- Verification:
  - `./gradlew :app:compileDebugKotlin` completed successfully after the interactivity changes.

## Profile/settings and navigation interactivity pass

- Changed Profile UI:
  - Added a visible `Настройки` action inside `ProfileScreen`.
  - Clicking `Настройки` opens a settings dialog.
  - The dialog contains editable `Login / email`, `New password`, and `Repeat password` fields.
  - `Сохранить` validates matching passwords in UI, calls `onUpdateCredentials(login, password)`, and relies on `ProfileUiState` to show success/error snackbar feedback.
  - `Отмена` closes the dialog without calling callbacks.
- Added Profile callbacks/state:
  - `ProfileScreen.onUpdateCredentials(login, password)`
  - `ProfileViewModel.onUpdateCredentials(login, password)`
  - `ProfileUiState.Success.credentialsMessage`
  - `ProfileUiState.Success.credentialsError`
- UI stubs by design:
  - Profile credentials update only updates UI state and snackbar messages.
  - Password is not persisted in UI state, Local Storage, or Data Layer.
  - No API request is made from Composables or ViewModel stubs.
- Changed Instrument chart behavior:
  - Renamed the period callback contract to `onPeriodSelected(period)` at the UI boundary.
  - `InstrumentViewModel.onPeriodSelected(period)` updates `selectedPeriod` and replaces mock `chartPoints`.
  - Added mock chart point sets for `1D`, `1W`, `1M`, `1Y`, and `ALL`.
  - Period buttons now visibly highlight the selected period, and chart redraws from changed state.
- Changed Portfolio UI/navigation:
  - `PortfolioItemCard` now accepts `onClick`.
  - `PortfolioScreen` now exposes `onPortfolioInstrumentClick(instrumentId)`.
  - `AppNavGraph` wires portfolio asset clicks to `InstrumentScreen` using the asset ticker.
  - Buy/Sell on instruments opened from Portfolio use the same confirmation dialog and snackbar flow as instruments opened from Market.
- Empty callback audit:
  - Checked presentation code for `onClick = {}` and `TODO()` placeholders.
  - No empty click handlers or `TODO()` calls remain in the Presentation Layer search scope.
- Developer 2 follow-up:
  - Replace profile credentials UI stub with secure credentials update use case and backend error mapping.
  - Connect chart periods to repository/API data when backend chart endpoints exist.
  - Keep portfolio-to-instrument navigation contract or replace it with a domain-owned instrument id if ticker is not sufficient.
  - Keep Buy/Sell submission outside Composables and map order success/error into `UiState`.
- Verification:
  - `./gradlew :app:compileDebugKotlin` completed successfully after these changes.
