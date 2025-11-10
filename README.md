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

## Features

- 🌍 **5 platforms** - Android, iOS, JVM, JS, WasmJs
- 💰 **Standard & ISO** - Symbol ($) or code (USD) formatting
- 🎨 **Compose ready** - State management with property delegation
- ✅ **Type-safe** - Result<String> for error handling
- 🌐 **Locale-aware** - Native formatting APIs
- 📝 **Observable** - Cedar Logger integration
- 🧪 **Tested** - 92 tests covering all scenarios

## Installation

```kotlin
dependencies {
    implementation("io.github.chilinoodles:kurrency:1.0.0")
}
```

## Quick Start

### Basic Usage

```kotlin
import com.chilinoodles.kurrency.Currency
import com.chilinoodles.kurrency.CurrencyStyle

val currency = Currency("USD")

val result: Result<String> = currency.formatAmount("1234.56")

val formatted = currency.formatAmountOrEmpty("1234.56")

val formatted by currency.format("1234.56")
println(formatted)
```

**Standard vs ISO Formatting:**
```kotlin
val currency = Currency("USD")

val standard by currency.format("1234.56")
println(standard)

val iso by currency.format("1234.56", CurrencyStyle.Iso)
println(iso)
```
Output:
- **Standard**: `$1,234.56` (uses currency symbol)
- **ISO**: `USD 1,234.56` (uses currency code)

### Compose Integration

**Simple approach (with automatic empty fallback):**

```kotlin
import com.chilinoodles.kurrency.rememberCurrencyState

@Composable
fun PriceDisplay() {
    val currencyState = rememberCurrencyState("EUR", "1234.56")
    
    Text(text = currencyState.formattedAmount)
}
```

**Result-based approach (full error control):**

```kotlin
@Composable
fun PriceDisplay() {
    val currencyState = rememberCurrencyState("EUR", "1234.56")
    
    currencyState.formattedAmountResult.fold(
        onSuccess = { formatted -> Text(formatted) },
        onFailure = { error -> Text("Error: ${error.errorMessage}") }
    )
}
```

**Using property delegation:**

```kotlin
@Composable
fun PriceDisplay() {
    val currencyState = rememberCurrencyState("EUR", "1234.56")
    val price by currencyState.formattedAmount()
    
    Text(text = price)
}
```

### Dynamic Updates

```kotlin
@Composable
fun CurrencyConverter() {
    val currencyState = rememberCurrencyState("USD", "100.00")
    
    Column {
        Text("Amount: ${currencyState.formattedAmount}")
        Button(onClick = { currencyState.updateCurrency("EUR") }) {
            Text("Switch to EUR")
        }
    }
}
```

## Optional Logging

Kurrency includes built-in logging via [Cedar Logger](https://github.com/ChiliNoodles/Cedar-Logger). By default, logs are not output anywhere. To enable logging, simply plant a tree:

```kotlin
import com.chilinoodles.cedar.logging.Cedar
import com.chilinoodles.cedar.logging.ConsoleTree

Cedar.plant(ConsoleTree())
```

### Logging Output

When enabled, you'll see detailed logs:

```
🐛 DEBUG   [Kurrency] Currency created: code=USD, fractionDigits=2
🐛 DEBUG   [Kurrency] Formatting amount: amount=1234.56, currency=USD, style=Standard
```

### Platform-Specific Logging

For production apps, use `PlatformLogTree` for native logging:

```kotlin
Cedar.plant(PlatformLogTree().configureForPlatform {
    iosSubsystem = "com.yourapp.kurrency"
    androidMaxLogLength = 2000
    enableEmojis = false
})
```

Cedar Logger is multiplatform and supports Android, iOS, JVM, and wasmJs. See the [Cedar Logger documentation](https://github.com/ChiliNoodles/Cedar-Logger) for more features.

## Error Handling

Kurrency uses Kotlin's `Result` type for type-safe error handling. Every formatting operation returns `Result<String>`, giving you full control over how to handle success and failure cases.

### Result-Based API

**Handling Results:**

```kotlin
val currency = Currency("USD")

currency.formatAmount("1234.56")
    .onSuccess { formatted ->
        println("Formatted: $formatted")
    }
    .onFailure { error ->
        when (error) {
            is KurrencyError.InvalidAmount -> println("Bad amount: ${error.amount}")
            is KurrencyError.FormattingFailure -> println("Format error: ${error.errorMessage}")
            else -> println("Error: ${error.errorMessage}")
        }
    }
```

**Using fold:**

```kotlin
val display = currency.formatAmount(userInput).fold(
    onSuccess = { it },
    onFailure = { "Invalid" }
)
```

**Get or Default:**

```kotlin
val formatted = currency.formatAmount("1234.56").getOrDefault("")

val formattedOrNull = currency.formatAmount("1234.56").getOrNull()

val formattedOrElse = currency.formatAmount("1234.56").getOrElse { "---" }
```

**Convenience Methods:**

```kotlin
val formatted = currency.formatAmountOrEmpty("1234.56")
```

### Error Types

```kotlin
sealed class KurrencyError : Exception {
    class InvalidCurrencyCode(val code: String)
    class InvalidAmount(val amount: String)
    class FormattingFailure(val currencyCode: String, val amount: String, cause: Throwable)
    class FractionDigitsFailure(val currencyCode: String, cause: Throwable)
}
```

### Compose Usage

**Option 1: Simple (automatic fallback to empty string):**

```kotlin
@Composable
fun Price() {
    val state = rememberCurrencyState("USD", "1234.56")
    Text(state.formattedAmount)
}
```

**Option 2: Result-based (full control):**

```kotlin
@Composable
fun Price() {
    val state = rememberCurrencyState("USD", userInput)
    
    state.formattedAmountResult.fold(
        onSuccess = { Text(it, style = MaterialTheme.typography.h4) },
        onFailure = { Text("Invalid amount", color = Color.Red) }
    )
}
```

**Option 3: Property delegation:**

```kotlin
@Composable
fun Price() {
    val state = rememberCurrencyState("USD", "1234.56")
    val price by state.formattedAmount()
    Text(price)
}
```

### Input Validation

**Validation Rules:**
- **Currency codes** must be exactly 3 letters (e.g., "USD", "EUR")
- **Amounts** must be valid numbers (supports both "." and "," as decimal separators)
- **Invalid inputs** return `Result.failure()` with appropriate `KurrencyError`

### Cedar Logger Integration

Enable logging to see validation warnings and errors during development:

```kotlin
Cedar.plant(ConsoleTree())

val result = Currency("XYZ").formatAmount("abc")
```

**Console output:**
```
⚠️  WARNING [Kurrency] Invalid currency code: XYZ
⚠️  WARNING [Kurrency] Invalid amount: abc
```

### Best Practices

**Development:**
```kotlin
Cedar.plant(ConsoleTree())
```

**Production with Analytics:**
```kotlin
currency.formatAmount(userInput)
    .onSuccess { formatted ->
        displayPrice(formatted)
    }
    .onFailure { error ->
        analytics.track("formatting_error", mapOf(
            "error_type" to error::class.simpleName,
            "message" to error.errorMessage
        ))
        if (error is KurrencyError.FormattingFailure) {
            crashlytics.recordException(error)
        }
        displayError("Please enter a valid amount")
    }
```

**UI Validation:**
```kotlin
TextField(
    value = amount,
    onValueChange = { newAmount ->
        amount = newAmount
        isValid = currency.formatAmount(newAmount).isSuccess
    },
    isError = !isValid
)
```

## API Reference

### Currency

```kotlin
val currency = Currency(code = "USD")

val result: Result<String> = currency.formatAmount("100.50")

val formatted: String = currency.formatAmountOrEmpty("100.50")

val isoResult: Result<String> = currency.formatAmount("100.50", CurrencyStyle.Iso)
```

**Properties:**
- `code: String` - Currency code (e.g., "USD")
- `fractionDigits: Int` - Decimal places for the currency

**Methods:**
- `formatAmount(amount: String, style: CurrencyStyle = Standard): Result<String>`
- `formatAmount(amount: Double, style: CurrencyStyle = Standard): Result<String>`
- `formatAmountOrEmpty(amount: String, style: CurrencyStyle = Standard): String`
- `formatAmountOrEmpty(amount: Double, style: CurrencyStyle = Standard): String`

### CurrencyState

```kotlin
val currencyState = rememberCurrencyState("EUR", "1234.56")
```

**Properties:**
- `formattedAmountResult: Result<String>` - Result-wrapped standard formatting
- `formattedAmountIsoResult: Result<String>` - Result-wrapped ISO formatting
- `formattedAmount: String` - Standard formatting (empty on error)
- `formattedAmountIso: String` - ISO formatting (empty on error)

**Methods:**
- `updateCurrency(currencyCode: String)`
- `updateAmount(newAmount: String)`
- `updateCurrencyAndAmount(currencyCode: String, newAmount: String)`

**Delegation:**
- `formattedAmount(style: CurrencyStyle = Standard)` - Property delegate

### CurrencyStyle

- `CurrencyStyle.Standard` - Regular formatting (e.g., "$100.00")
- `CurrencyStyle.Iso` - ISO formatting (e.g., "USD 100.00")

## Examples

### Different Currencies

```kotlin
val usd = Currency("USD").formatAmountOrEmpty("1234.56")
val eur = Currency("EUR").formatAmountOrEmpty("1234.56")
val jpy = Currency("JPY").formatAmountOrEmpty("1234")
```

### Shopping Cart with Error Handling

```kotlin
@Composable
fun ShoppingCart(items: List<CartItem>) {
    val currencyState = rememberCurrencyState("USD")
    
    LaunchedEffect(items) {
        val total = items.sumOf { it.price }
        currencyState.updateAmount(total.toString())
    }
    
    currencyState.formattedAmountResult.fold(
        onSuccess = { Text("Total: $it", style = MaterialTheme.typography.h5) },
        onFailure = { Text("Error calculating total", color = Color.Red) }
    )
}
```

### Form Validation

```kotlin
@Composable
fun PriceInput() {
    var amount by remember { mutableStateOf("") }
    val currency = remember { Currency("USD") }
    
    Column {
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            isError = amount.isNotEmpty() && 
                     currency.formatAmount(amount).isFailure
        )
        
        currency.formatAmount(amount).onSuccess { formatted ->
            Text("Preview: $formatted", color = Color.Green)
        }
    }
}
```

### Web Usage (JS/WasmJs)

```kotlin
fun main() {
    val currency = Currency("EUR")
    val formatted = currency.formatAmount("1234.56")
    console.log(formatted)
    
    document.getElementById("price")?.innerHTML = formatted
}
```

The web implementations use the browser's native `Intl.NumberFormat` API, providing:
- Automatic locale detection
- Standards-compliant formatting
- Zero dependencies
- Excellent performance

### iOS Swift Integration

When using Kurrency in your Kotlin Multiplatform project with iOS, export the module in your shared framework:

```kotlin
listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
        export("io.github.chilinoodles:kurrency:1.0.0")
        baseName = "YourFramework"
        isStatic = true
    }
}
```

Then use it directly in Swift:

```swift
import YourFramework

let currency = Currency(code: "USD")

let result = currency.formatAmount(amount: "1234.56")
if case .success(let formatted) = result {
    print("Formatted: \(formatted)")
} else if case .failure(let error) = result {
    print("Error: \(error.localizedDescription)")
}

let formatted = currency.formatAmountOrEmpty(amount: "1234.56")
```

**With Pattern Matching:**

```swift
let result = currency.formatAmount(amount: userInput)

switch result {
case .success(let formatted):
    priceLabel.text = formatted
case .failure(let error as KurrencyError.InvalidAmount):
    showError("Please enter a valid amount")
case .failure(let error):
    print("Formatting error: \(error.localizedDescription)")
}
```

**SwiftUI Integration:**

```swift
struct PriceView: View {
    @State private var amount = "1234.56"
    
    var body: some View {
        let currency = Currency(code: "EUR")
        Text(currency.formatAmount(amount: amount))
            .font(.title)
    }
}
```

**Enable Logging in iOS:**

```swift
import YourFramework

Cedar.shared.plant(tree: ConsoleTree())

let currency = Currency(code: "USD")
currency.formatAmount(amount: "1234.56")
```

Cedar Logger is automatically exported with the framework, so all logging features are available in Swift.

## Supported Platforms

- ✅ **Android** (API 24+) - Native ICU formatting
- ✅ **iOS** (iOS 13+) - NSNumberFormatter
- ✅ **JVM** (Java 17+) - java.text.NumberFormat
- ✅ **JS** (Browser/Node.js) - Intl.NumberFormat API
- ✅ **WasmJs** (Browser) - Intl.NumberFormat API

## Architecture

**Platform-Specific Implementations:**
- **Android**: Uses `android.icu.text.NumberFormat` for native formatting
- **iOS**: Uses `NSNumberFormatter` for native formatting
- **JVM**: Uses `java.text.NumberFormat` and `java.util.Currency`
- **JS/WasmJs**: Uses JavaScript's `Intl.NumberFormat` API

All formatting operations include validation and error handling with sensible defaults on failure.

## License

Apache License 2.0 - Copyright © 2025

## Contributing

Contributions welcome! Submit a Pull Request or open an issue.
=======
This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop (JVM).

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.
>>>>>>> origin/main
