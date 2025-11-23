# Kurrency 💱

![CI](https://github.com/ChiliNoodles/Kurrency/workflows/CI/badge.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-blue.svg?style=flat&logo=kotlin)
![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin_Multiplatform-2.2.0-blue.svg?style=flat&logo=kotlin)
![Android](https://img.shields.io/badge/Android-24%2B-green.svg?style=flat&logo=android)
![iOS](https://img.shields.io/badge/iOS-13%2B-lightgrey.svg?style=flat&logo=apple)
![JVM](https://img.shields.io/badge/JVM-17%2B-orange.svg?style=flat&logo=openjdk)
![JS](https://img.shields.io/badge/JS-IR-yellow.svg?style=flat&logo=javascript)
![WasmJs](https://img.shields.io/badge/WasmJs-✓-purple.svg?style=flat&logo=webassembly)
![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg?style=flat)

Type-safe currency formatting for Kotlin Multiplatform with Compose support and comprehensive locale management.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Locale Management](#locale-management)
- [Compose Integration](#compose-integration)
- [API Reference](#api-reference)
- [Error Handling](#error-handling)
- [Platform Support](#platform-support)
- [License](#license)

## Features

- 🌍 **Multi-platform support** - Android, iOS, JVM, JS, WasmJs
- 🌐 **Locale management** - Format currencies for any locale
- 🎨 **Compose integration** - Ready-to-use Composables with reactive locale updates
- ✅ **Type-safe error handling** - Result-based API
- 🔄 **Multiple format styles** - Standard currency symbols or ISO codes
- 📦 **Lightweight** - Minimal dependencies

## Installation

### Core Library

```kotlin
dependencies {
    implementation("io.github.chilinoodles:kurrency:1.0.0")
}
```

### Compose Integration (Optional)

```kotlin
dependencies {
    implementation("io.github.chilinoodles:kurrency:1.0.0")
    implementation("io.github.chilinoodles:kurrency-compose:1.0.0")
}
```

## Quick Start

### Basic Usage (System Locale)

```kotlin
import com.chilinoodles.kurrency.CurrencyFormatter

// Using the singleton with system locale
val result: Result<String> = CurrencyFormatter.formatCurrencyStyle("1234.56", "USD")
val formatted = result.getOrNull() // "$1,234.56" (in en-US locale)

// Get fraction digits for a currency
val fractionDigits = CurrencyFormatter.getFractionDigits("USD").getOrNull() // 2
```

### Formatting Styles

```kotlin
// Standard currency format (with symbol)
CurrencyFormatter.formatCurrencyStyle("1234.56", "USD")
// Result: "$1,234.56" (US), "1.234,56 $" (DE)

// ISO format (with currency code)
CurrencyFormatter.formatIsoCurrencyStyle("1234.56", "USD")
// Result: "USD 1,234.56"
```

## Locale Management

### Using Predefined Locales

```kotlin
import com.chilinoodles.kurrency.CurrencyFormatter
import com.chilinoodles.kurrency.KurrencyLocale

// Create formatters for specific locales
val usFormatter = CurrencyFormatter.create(KurrencyLocale.US)
val germanFormatter = CurrencyFormatter.create(KurrencyLocale.GERMANY)
val japaneseFormatter = CurrencyFormatter.create(KurrencyLocale.JAPAN)

// Format the same amount in different locales
usFormatter.formatCurrencyStyle("1234.56", "USD")      // "$1,234.56"
germanFormatter.formatCurrencyStyle("1234.56", "EUR")  // "1.234,56 €"
japaneseFormatter.formatCurrencyStyle("1234.56", "JPY") // "¥1,235"
```

### Available Predefined Locales

```kotlin
KurrencyLocale.US              // en-US
KurrencyLocale.UK              // en-GB
KurrencyLocale.CANADA          // en-CA
KurrencyLocale.CANADA_FRENCH   // fr-CA
KurrencyLocale.GERMANY         // de-DE
KurrencyLocale.FRANCE          // fr-FR
KurrencyLocale.ITALY           // it-IT
KurrencyLocale.SPAIN           // es-ES
KurrencyLocale.JAPAN           // ja-JP
KurrencyLocale.CHINA           // zh-CN
KurrencyLocale.KOREA           // ko-KR
KurrencyLocale.BRAZIL          // pt-BR
KurrencyLocale.RUSSIA          // ru-RU
KurrencyLocale.SAUDI_ARABIA    // ar-SA
KurrencyLocale.INDIA           // hi-IN
```

### Custom Locales from Language Tags

```kotlin
// Create locale from BCP 47 language tag
val locale = KurrencyLocale.fromLanguageTag("de-AT").getOrNull() // German (Austria)
val formatter = CurrencyFormatter.create(locale)
```

### System Locale

```kotlin
// Get the device's current locale
val systemLocale = KurrencyLocale.systemLocale()
val formatter = CurrencyFormatter.createWithSystemLocale()
```

### Integration with Compose Multiplatform Locale

```kotlin
import androidx.compose.ui.text.intl.Locale
import com.chilinoodles.kurrency.toKurrencyLocale

@Composable
fun MyComposable() {
    val composeLocale = Locale.current
    val kurrencyLocale = composeLocale.toKurrencyLocale().getOrNull()
    val formatter = kurrencyLocale?.let { CurrencyFormatter.create(it) }
}
```

## Compose Integration

Add the `kurrency-compose` dependency for Jetpack Compose Multiplatform support.

### Using rememberCurrencyFormatter

The formatter automatically recreates when the locale changes (key-based recomposition).

```kotlin
import com.chilinoodles.kurrency.compose.rememberCurrencyFormatter
import com.chilinoodles.kurrency.KurrencyLocale

@Composable
fun PriceDisplay(amount: String, currencyCode: String) {
    var selectedLocale by remember { mutableStateOf(KurrencyLocale.US) }

    // Formatter recreates when locale changes
    val formatter = rememberCurrencyFormatter(locale = selectedLocale)

    val formattedPrice = remember(amount, currencyCode) {
        formatter.formatCurrencyStyle(amount, currencyCode).getOrNull() ?: ""
    }

    Column {
        Text("Price: $formattedPrice")

        Button(onClick = { selectedLocale = KurrencyLocale.GERMANY }) {
            Text("Switch to German locale")
        }
    }
}
```

### Using LocalCurrencyFormatter (CompositionLocal)

Provide a formatter for an entire subtree of your composition.

```kotlin
import com.chilinoodles.kurrency.compose.ProvideCurrencyFormatter
import com.chilinoodles.kurrency.compose.LocalCurrencyFormatter
import com.chilinoodles.kurrency.KurrencyLocale

@Composable
fun App() {
    var appLocale by remember { mutableStateOf(KurrencyLocale.US) }

    ProvideCurrencyFormatter(locale = appLocale) {
        // All child composables can access the formatter
        HomeScreen()
        ProductScreen()
    }
}

@Composable
fun ProductScreen() {
    // Access the provided formatter
    val formatter = LocalCurrencyFormatter.current

    val price = remember {
        formatter.formatCurrencyStyle("99.99", "USD").getOrNull() ?: ""
    }

    Text("Price: $price")
}
```

### Reactive Locale Updates

Combine with Compose's State system for dynamic locale switching:

```kotlin
@Composable
fun MultiCurrencyDisplay() {
    var locale by remember { mutableStateOf(KurrencyLocale.US) }
    val formatter = rememberCurrencyFormatter(locale)

    val prices = listOf(
        "USD" to "100.00",
        "EUR" to "85.50",
        "JPY" to "11000"
    )

    Column {
        prices.forEach { (currency, amount) ->
            val formatted = remember(locale, currency, amount) {
                formatter.formatCurrencyStyle(amount, currency).getOrNull() ?: ""
            }
            Text(formatted)
        }

        Row {
            Button(onClick = { locale = KurrencyLocale.US }) { Text("US") }
            Button(onClick = { locale = KurrencyLocale.UK }) { Text("UK") }
            Button(onClick = { locale = KurrencyLocale.JAPAN }) { Text("JP") }
        }
    }
}
```

## API Reference

### CurrencyFormatter (Singleton)

```kotlin
// Format with system locale
CurrencyFormatter.formatCurrencyStyle(amount: String, currencyCode: String): Result<String>
CurrencyFormatter.formatIsoCurrencyStyle(amount: String, currencyCode: String): Result<String>
CurrencyFormatter.getFractionDigits(currencyCode: String): Result<Int>
CurrencyFormatter.getFractionDigitsOrDefault(currencyCode: String): Int

// Create instances with custom locales
CurrencyFormatter.create(locale: KurrencyLocale? = null): CurrencyFormat
CurrencyFormatter.createWithSystemLocale(): CurrencyFormat
```

### CurrencyFormat (Interface)

```kotlin
interface CurrencyFormat {
    fun getFractionDigits(currencyCode: String): Result<Int>
    fun formatCurrencyStyle(amount: String, currencyCode: String): Result<String>
    fun formatIsoCurrencyStyle(amount: String, currencyCode: String): Result<String>
}
```

### KurrencyLocale

```kotlin
// Create from language tag
KurrencyLocale.fromLanguageTag(languageTag: String): Result<KurrencyLocale>

// Get system locale
KurrencyLocale.systemLocale(): KurrencyLocale

// Predefined locales
KurrencyLocale.US, UK, CANADA, GERMANY, FRANCE, JAPAN, etc.

// Properties
val languageTag: String  // e.g., "en-US"
```

### Compose Extensions (kurrency-compose)

```kotlin
// Remember formatter with specific locale
@Composable
fun rememberCurrencyFormatter(locale: KurrencyLocale): CurrencyFormat

// Remember formatter with system locale
@Composable
fun rememberSystemCurrencyFormatter(): CurrencyFormat

// CompositionLocal
val LocalCurrencyFormatter: CompositionLocal<CurrencyFormat>

// Provider composables
@Composable
fun ProvideCurrencyFormatter(locale: KurrencyLocale, content: @Composable () -> Unit)

@Composable
fun ProvideSystemCurrencyFormatter(content: @Composable () -> Unit)

// Compose Locale extensions
fun Locale.toKurrencyLocale(): Result<KurrencyLocale>
fun KurrencyLocale.Companion.fromComposeLocale(composeLocale: Locale): Result<KurrencyLocale>

@Composable
fun KurrencyLocale.Companion.current(): KurrencyLocale
```

## Error Handling

All formatting methods return `Result<String>` for type-safe error handling:

```kotlin
val formatter = CurrencyFormatter.create(KurrencyLocale.US)

formatter.formatCurrencyStyle("1234.56", "USD")
    .onSuccess { formatted -> println(formatted) }
    .onFailure { error ->
        when (error) {
            is KurrencyError.InvalidAmount -> println("Invalid amount")
            is KurrencyError.InvalidCurrencyCode -> println("Invalid currency")
            else -> println("Formatting error")
        }
    }
```

### Error Types

- `KurrencyError.InvalidCurrencyCode` - Invalid currency code format
- `KurrencyError.InvalidAmount` - Invalid amount format
- `KurrencyError.FormattingFailure` - Platform formatting error
- `KurrencyError.FractionDigitsFailure` - Failed to get fraction digits

## Platform Support

- ✅ **Android** (API 24+) - Native ICU formatting
- ✅ **iOS** (iOS 13+) - NSNumberFormatter
- ✅ **JVM** (Java 17+) - java.text.NumberFormat
- ✅ **JS** (Browser/Node.js) - Intl.NumberFormat API
- ✅ **WasmJs** (Browser) - Intl.NumberFormat API

## License

Apache License 2.0 - Copyright © 2025
