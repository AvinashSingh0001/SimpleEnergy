# Simple Energy – Vehicle Dashboard

Android assignment app that shows a fleet **vehicle list** and **vehicle details**, backed by a Retrofit REST layer with mock JSON responses.

## Build and run

**Requirements:** Android Studio Ladybug or newer, JDK 11+, Android SDK 36.

1. Open the project folder in Android Studio.
2. Sync Gradle.
3. Run the `app` configuration on an emulator or device (API 24+).

From the command line:

```bash
./gradlew assembleDebug
./gradlew test
```

## Architecture

Single-module app with package-level clean architecture:

| Layer | Responsibility |
|--------|----------------|
| **presentation** | Jetpack Compose UI, Navigation, ViewModels (`StateFlow`) |
| **domain** | Models, repository contract, use cases |
| **data** | Retrofit API, DTOs, mappers, repository implementation |

**Data flow:** UI → ViewModel → Use case → Repository → Retrofit → (mock) JSON.

**REST / mock API:** `VehicleApiService` defines `GET /vehicles` and `GET /vehicles/{id}`. At runtime, `AssetMockInterceptor` serves responses from `app/src/main/assets/mock/vehicles.json` so the app works offline while still exercising Retrofit, Moshi, and HTTP error handling. Swap the base URL and remove the asset interceptor to point at a real backend.

**State management:** ViewModels expose `StateFlow` UI state; screens collect with `collectAsStateWithLifecycle()`. Coroutines run in `viewModelScope`.

**Dependency wiring:** Manual composition root in `AppContainer` (no Hilt), attached via `SimpleEnergyApplication`.

## Screens

- **Vehicle list** – name, model, battery %, range, online/offline; tap to open details.
- **Vehicle details** – full metrics, connectivity, last updated, toolbar refresh.

Edge cases handled: loading, empty list, network/server/not-found errors, retry, refresh while keeping last good detail on failure.

## Tests

Unit tests (JUnit + MockWebServer + MockK):

- `VehicleRepositoryImplTest` – JSON mapping and 404 → domain error.
- `VehicleListViewModelTest` – success and failure UI state.

## Assumptions and limitations

- Mock data is static JSON in assets; timestamps are fixed sample values.
- No local database cache or dependency injection framework (optional enhancements).
- UI is intentionally simple; focus is architecture and correctness.
- Internet permission is declared for parity with a real API; mock interceptor does not require network access.
