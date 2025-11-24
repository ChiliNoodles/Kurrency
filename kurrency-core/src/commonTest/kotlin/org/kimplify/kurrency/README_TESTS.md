# Kurrency Test Suite

Comprehensive test coverage for the Kurrency library's core functionality.

## Test Files

### 1. CurrencyTest.kt
Tests the `Currency` class functionality.

**Coverage:**
- ✅ Currency creation and properties
- ✅ Fraction digits for different currencies (USD, JPY, etc.)
- ✅ Format amount with Result<String> return type
- ✅ Standard vs ISO formatting styles
- ✅ Double and String amount inputs
- ✅ Invalid inputs (empty, malformed, invalid currency codes)
- ✅ Edge cases (zero, negative, very large numbers)
- ✅ Comma decimal separator support
- ✅ Property delegation
- ✅ Multiple international currencies (EUR, GBP, JPY, CHF)
- ✅ FormatAmountOrEmpty convenience method

**Total Tests:** 27

### 2. CurrencyFormatterTest.kt
Tests the `CurrencyFormatter` singleton functionality.

**Coverage:**
- ✅ Get fraction digits for various currencies
- ✅ Get fraction digits with fallback defaults
- ✅ Format currency style (standard symbol formatting)
- ✅ Format ISO currency style (currency code formatting)
- ✅ Input validation (currency codes and amounts)
- ✅ Error handling (Result<T> failures)
- ✅ Edge cases (zero, negative, blank inputs)
- ✅ Comma vs dot decimal separators
- ✅ Multiple currency support
- ✅ Comprehensive validation rules

**Total Tests:** 26

### 3. CurrencyStateTest.kt
Tests the `CurrencyState` class for Compose integration.

**Coverage:**
- ✅ State creation and initialization
- ✅ Default values
- ✅ Formatted amount properties
- ✅ Result-based properties (formattedAmountResult)
- ✅ Update operations (currency, amount, both)
- ✅ State reactivity after updates
- ✅ Invalid input handling
- ✅ Edge cases (zero, negative amounts)
- ✅ Multiple sequential updates
- ✅ Property delegation support
- ✅ Multiple currencies
- ✅ State immutability

**Total Tests:** 26

### 4. KurrencyErrorTest.kt
Tests the `KurrencyError` sealed class hierarchy.

**Coverage:**
- ✅ InvalidCurrencyCode error
- ✅ InvalidAmount error
- ✅ FormattingFailure error (with cause)
- ✅ FractionDigitsFailure error (with cause)
- ✅ Error is Exception/Throwable
- ✅ Error can be caught in try-catch
- ✅ Errors in Result<T> types
- ✅ Error messages are accessible
- ✅ Empty value handling
- ✅ Unique error messages

**Total Tests:** 13

## Running Tests

### Run All Tests
```bash
./gradlew test
```

### Run Specific Platform Tests
```bash
# Android
./gradlew testDebugUnitTest

# JVM
./gradlew jvmTest

# iOS
./gradlew iosSimulatorArm64Test

# JS
./gradlew jsTest

# WasmJs
./gradlew wasmJsTest
```

### Run Specific Test Class
```bash
./gradlew test --tests "CurrencyTest"
./gradlew test --tests "CurrencyFormatterTest"
```

## Test Coverage Summary

| Component | Tests | Coverage Areas |
|-----------|-------|----------------|
| Currency | 27 | Creation, formatting, validation, delegation |
| CurrencyFormatter | 26 | Low-level formatting, validation, errors |
| CurrencyState | 26 | State management, updates, Compose integration |
| KurrencyError | 13 | Error types, messages, Result integration |
| **Total** | **92** | **Comprehensive coverage** |

## Key Test Scenarios

### ✅ Happy Path
- Valid currency codes and amounts
- Standard and ISO formatting
- Multiple international currencies
- State updates and reactivity

### ✅ Error Handling
- Invalid currency codes (too short, too long, non-letters)
- Invalid amounts (empty, blank, non-numeric)
- Result<T> failure cases
- Error message validation

### ✅ Edge Cases
- Zero amounts
- Negative amounts
- Very large numbers
- Comma vs dot decimal separators
- Empty strings
- Sequential state updates

### ✅ Integration
- Property delegation
- Compose state management
- Result-based API
- Error propagation

## Adding New Tests

When adding new features, ensure:

1. **Happy path tests** - Valid inputs produce expected outputs
2. **Error cases** - Invalid inputs produce appropriate errors
3. **Edge cases** - Boundary conditions are handled
4. **Integration** - New features work with existing functionality

Example:
```kotlin
@Test
fun testNewFeature() {
    // Arrange
    val currency = Currency("USD")
    
    // Act
    val result = currency.newFeature("input")
    
    // Assert
    assertTrue(result.isSuccess)
    assertNotNull(result.getOrNull())
}
```

## CI/CD Integration

Tests run automatically on:
- Every push to main/develop
- Every pull request
- Before Maven Central publication
- Manual workflow dispatch

See `.github/workflows/build.yml` for CI configuration.

