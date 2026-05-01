# Солнышко дня ☀️

---

## Для пользователей

**Солнышко дня** — это маленькое тёплое приложение, которое каждый день выбирает «солнышко дня».

### Как это работает

1. Открой приложение — тебя встретит уютный экран с солнышком.
2. Нажми кнопку **«Узнать»** — приложение на секунду задумается.
3. И вот ответ! Включается фронтальная камера, вокруг летит конфетти, появляются весёлые питомцы.

### Что нужно приложению

- Разрешение на доступ к камере — без него не получится увидеть себя в роли солнышка дня.

### Поддерживаемые платформы

- Android 7.0 и выше
- iOS (iPhone / iPad)

---

## Для разработчиков

### Стек технологий

| Компонент | Версия |
|---|---|
| Kotlin | 2.3.20 |
| Kotlin Multiplatform | — |
| Compose Multiplatform | 1.10.3 |
| Material 3 | 1.10.0-alpha05 |
| CameraX | 1.4.2 |
| Coil | 3.1.0 |
| Android Gradle Plugin | 8.11.2 |

### Платформы

- **Android** — `minSdk 24`, `targetSdk / compileSdk 36`, JVM 11
- **iOS** — `iosArm64`, `iosSimulatorArm64`, статический фреймворк `ComposeApp`

### Структура проекта

```
Sunofday/
├── composeApp/                        # Основной мультиплатформенный модуль
│   └── src/
│       ├── commonMain/                # Общий код: UI, состояние, бизнес-логика
│       │   └── kotlin/.../
│       │       ├── ui/
│       │       │   ├── SunshineScreen.kt          # Главный экран
│       │       │   └── components/
│       │       │       ├── SunshineCircle.kt      # Круг с камерой / заглушкой
│       │       │       ├── SunshineButton.kt      # Кнопка с анимацией текста
│       │       │       └── CuteDecorations.kt     # Canvas-декорации (звёздочки)
│       │       ├── state/
│       │       │   ├── SunshineScreenState.kt     # sealed class состояний
│       │       │   └── SunshineScreenController.kt
│       │       └── platform/
│       │           ├── CameraPermission.kt        # expect: запрос / проверка разрешения
│       │           ├── FrontCameraPreview.kt      # expect: превью фронтальной камеры
│       │           └── SoundPlayer.kt             # expect: воспроизведение звука
│       ├── androidMain/               # Android-реализации expect-функций
│       └── iosMain/                   # iOS-реализации expect-функций
└── iosApp/                            # iOS-точка входа (Swift / SwiftUI)
```

### Архитектура

Приложение построено на простом конечном автомате состояний без внешних архитектурных фреймворков:

```
Initial ──[клик «Узнать»]──► Loading ──[2 сек. задержка]──► Result
  ▲                                                              │
  └──────────────────────[клик «Назад»]──────────────────────────┘
```

- **`SunshineScreenState`** — `sealed class` с тремя состояниями: `Initial`, `Loading`, `Result`.
- **`SunshineScreenController`** — владеет состоянием через `mutableStateOf`, управляет переходами.
- **`SunshineScreen`** — единственный Composable-экран, подписывается на состояние контроллера напрямую.

### Ключевые технические решения

**Камера (Android)**
- `CameraX` с `CameraSelector.LENS_FACING_FRONT` и `PreviewView.ScaleType.FILL_CENTER`.
- `FrontCameraPreview` встраивается через `AndroidView`.

**Камера (iOS)**
- `AVCaptureSession` с `AVCaptureDevice` фронтальной камеры.
- Превью реализовано через `UIViewRepresentable`-обёртку (`AVCaptureVideoPreviewLayer`).

**Разрешение на камеру**
- `rememberHasCameraPermission()` — реактивная проверка статуса разрешения.
- На Android обновляется через `LifecycleEventObserver` при каждом `ON_RESUME` (учитывает возврат из Настроек).
- На iOS использует `AVCaptureDevice.authorizationStatusForMediaType`.

**GIF-анимации**
- Загружаются через `Coil 3` с `GifDecoder` (Android) и нативным декодером (iOS).
- Хранятся в `commonMain/composeResources/files/`.

**Иконка приложения**
- Адаптивная иконка (API 26+): векторный фон-градиент (`#FFF8F0` → `#FFEDD8`) + векторное солнце (`#FF8F00`) с 8 лучами.
- Legacy PNG-иконки сгенерированы для всех плотностей: mdpi → xxxhdpi.

### Сборка и запуск

**Android**

```bash
# Debug APK
./gradlew :composeApp:assembleDebug

# Установить на подключённое устройство
./gradlew :composeApp:installDebug
```

**iOS**

Открой папку `iosApp/` в Xcode и запусти через стандартный Run (⌘R) или выбери симулятор в IDE.

### Зависимости

Все версии управляются через Version Catalog — файл `gradle/libs.versions.toml`.

```toml
[versions]
kotlin              = "2.3.20"
composeMultiplatform = "1.10.3"
camerax             = "1.4.2"
coil                = "3.1.0"
androidx-lifecycle  = "2.10.0"
```
