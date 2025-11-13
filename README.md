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

Type-safe currency formatting for Kotlin Multiplatform with Compose support.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Compose Integration](#compose-integration)
- [API Reference](#api-reference)
- [Error Handling](#error-handling)
- [Platform Support](#platform-support)
- [License](#license)

## Features

- Multi-platform support (Android, iOS, JVM, JS, WasmJs)
- Compose integration
- Type-safe error handling

## Installation

```kotlin
dependencies {
    implementation("io.github.chilinoodles:kurrency:0.1.0")
}
```

## Quick Start

### Basic Usage

```kotlin
import com.chilinoodles.kurrency.Currency

val currency = Currency("USD")

val result: Result<String> = currency.formatAmount("1234.56")
val formatted = currency.formatAmountOrEmpty("1234.56")
```

### Formatting Styles

```kotlin
val currency = Currency("USD")

currency.formatAmount("1234.56", CurrencyStyle.Standard) // "$1,234.56"
currency.formatAmount("1234.56", CurrencyStyle.Iso)      // "USD 1,234.56"
```

### Validation

```kotlin
Currency.isValidCode("USD")              // true
CurrencyMetadata.isSupported("USD")     // true
```

## Compose Integration

```kotlin
import com.chilinoodles.kurrency.rememberCurrencyState

@Composable
fun PriceDisplay() {
    val currencyState = rememberCurrencyState("EUR", "1234.56")
    
    Text(text = currencyState.formattedAmount)
    
    Button(onClick = { currencyState.updateCurrency("USD") }) {
        Text("Switch to USD")
    }
}
```

### Property Delegation

```kotlin
@Composable
fun PriceDisplay() {
    val currencyState = rememberCurrencyState("USD", "1234.56")
    val price by currencyState.formattedAmount()
    
    Text(text = price)
}
```

## API Reference

### Currency

```kotlin
val currency = Currency(code = "USD")

currency.formatAmount(amount: String, style: CurrencyStyle = Standard): Result<String>
currency.formatAmount(amount: Double, style: CurrencyStyle = Standard): Result<String>
currency.formatAmountOrEmpty(amount: String, style: CurrencyStyle = Standard): String
currency.formatAmountOrEmpty(amount: Double, style: CurrencyStyle = Standard): String

Currency.isValidCode(code: String): Boolean
```

### CurrencyState

```kotlin
val currencyState = rememberCurrencyState(currencyCode: String, amount: String?)

currencyState.formattedAmount: String
currencyState.formattedAmountResult: Result<String>
currencyState.updateCurrency(currencyCode: String)
currencyState.updateAmount(newAmount: String)
```

### CurrencyMetadata

```kotlin
CurrencyMetadata.isSupported(currencyCode: String): Boolean
```

## Error Handling

All formatting methods return `Result<String>` for type-safe error handling:

```kotlin
currency.formatAmount("1234.56")
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
