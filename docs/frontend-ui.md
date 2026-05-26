# Frontend UI Layer

## Scope

This app now has a dedicated Presentation Layer under:

`app/src/main/java/com/example/rmp_frontend/presentation`

The layer contains Compose UI, screens, navigation, UI state models, visual components, and AndroidX ViewModels. It does not call backend APIs, does not create API clients in composables, does not store tokens, and does not implement trading business rules.

## Navigation

Navigation entry point:

`presentation/navigation/AppNavGraph.kt`

Routes:

- `Splash`
- `Auth`
- `Market`
- `Instrument`
- `Portfolio`
- `History`
- `Profile`

Flow:

- Splash routes to Auth by default because token/local-storage integration is not implemented in Developer 1 scope.
- Auth routes to Market after successful UI-level submit.
- Main tabs are Market, Portfolio, History, Profile.
- Market opens Instrument details by ticker.
- Profile logout routes back to Auth.

## UI State

State files are in `presentation/state`.

List/detail screens use explicit states:

- `Loading`
- `Success`
- `Error`
- `Empty`

Screens render only their `UiState` and send user events to callbacks/ViewModels.

## ViewModels

ViewModels are in `presentation/viewmodel`.

They expose `StateFlow<UiState>` and receive UI events such as:

- `onLoginClick`
- `onRegisterClick`
- `onInstrumentClick` through navigation callback
- `onBuyClick`
- `onSellClick`
- `onLogoutClick`
- `onRefreshClick`

Current ViewModels use local in-memory UI sample data only to keep the frontend compileable and navigable. Backend, API client, WebSocket, token storage, and real trading actions must be connected by Developer 2 through the Data Layer.

## Components

Reusable Compose components are in `presentation/components`:

- `AppButton`
- `AppTextField`
- `LoadingView`
- `ErrorView`
- `EmptyView`
- `InstrumentCard`
- `PortfolioItemCard`
- `TransactionItem`
- `PriceChangeBadge`
- `AppTopBar`

## Design

The app uses a dark Material 3 theme with card-based lists, yellow accent actions, green positive price movement, red negative movement, and bottom navigation for the main screens.
