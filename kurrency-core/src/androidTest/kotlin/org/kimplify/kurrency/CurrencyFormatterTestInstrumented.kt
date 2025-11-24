package org.kimplify.kurrency
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class CurrencyFormatterTestInstrumented {
    
    @Test
    fun testGetFractionDigitsForUSD() {
        val result = CurrencyFormatter.getFractionDigits("USD")
        
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull())
    }
    
    @Test
    fun testGetFractionDigitsForJPY() {
        val result = CurrencyFormatter.getFractionDigits("JPY")
        
        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull())
    }
    
    @Test
    fun testGetFractionDigitsForInvalidCurrency() {
        val result = CurrencyFormatter.getFractionDigits("INVALID")
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is KurrencyError.InvalidCurrencyCode)
    }
    
    @Test
    fun testGetFractionDigitsForShortCode() {
        val result = CurrencyFormatter.getFractionDigits("US")
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is KurrencyError.InvalidCurrencyCode)
    }
    
    @Test
    fun testGetFractionDigitsOrDefaultForUSD() {
        val fractionDigits = CurrencyFormatter.getFractionDigitsOrDefault("USD")
        
        assertEquals(2, fractionDigits)
    }
    
    @Test
    fun testGetFractionDigitsOrDefaultForInvalidCurrency() {
        val fractionDigits = CurrencyFormatter.getFractionDigitsOrDefault("INVALID")
        
        assertEquals(2, fractionDigits)
    }
    
    @Test
    fun testFormatCurrencyStyleSuccess() {
        val result = CurrencyFormatter.formatCurrencyStyle("100.50", "USD")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testFormatIsoCurrencyStyleSuccess() {
        val result = CurrencyFormatter.formatIsoCurrencyStyle("100.50", "USD")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testFormatCurrencyStyleWithInvalidAmount() {
        val result = CurrencyFormatter.formatCurrencyStyle("invalid", "USD")
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is KurrencyError.InvalidAmount)
    }
    
    @Test
    fun testFormatCurrencyStyleWithInvalidCurrency() {
        val result = CurrencyFormatter.formatCurrencyStyle("100.00", "INVALID")
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is KurrencyError.InvalidCurrencyCode)
    }
    
    @Test
    fun testFormatWithEmptyAmount() {
        val result = CurrencyFormatter.formatCurrencyStyle("", "USD")
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is KurrencyError.InvalidAmount)
    }
    
    @Test
    fun testFormatWithBlankAmount() {
        val result = CurrencyFormatter.formatCurrencyStyle("   ", "USD")
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is KurrencyError.InvalidAmount)
    }
    
    @Test
    fun testFormatWithZero() {
        val result = CurrencyFormatter.formatCurrencyStyle("0", "USD")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testFormatWithNegative() {
        val result = CurrencyFormatter.formatCurrencyStyle("-50.25", "USD")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testFormatWithCommaDecimalSeparator() {
        val result = CurrencyFormatter.formatCurrencyStyle("100,50", "EUR")

        print(result.exceptionOrNull())
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testFormatMultipleCurrencies() {
        val currencies = listOf("USD", "EUR", "GBP", "JPY", "CHF", "CAD", "AUD")
        
        currencies.forEach { code ->
            val result = CurrencyFormatter.formatCurrencyStyle("100.00", code)
            assertTrue(result.isSuccess, "Failed to format $code")
        }
    }
    
    @Test
    fun testCurrencyCodeValidation() {
        val invalidCodes = listOf("", "US", "USDD", "123", "US1", "us$")
        
        invalidCodes.forEach { code ->
            val result = CurrencyFormatter.formatCurrencyStyle("100.00", code)
            assertTrue(result.isFailure, "Should fail for invalid code: $code")
        }
    }
    
    @Test
    fun testAmountValidation() {
        val invalidAmounts = listOf("", "   ", "abc", "12.34.56", "12,34.56", "NaN")
        
        invalidAmounts.forEach { amount ->
            val result = CurrencyFormatter.formatCurrencyStyle(amount, "USD")
            assertTrue(result.isFailure, "Should fail for invalid amount: $amount")
        }
    }
    
    @Test
    fun testValidAmountFormats() {
        val validAmounts = listOf("0", "1", "100", "100.5", "100.50", "1234.56", "-100", "999999.99")
        
        validAmounts.forEach { amount ->
            val result = CurrencyFormatter.formatCurrencyStyle(amount, "USD")
            assertTrue(result.isSuccess, "Should succeed for valid amount: $amount")
        }
    }
}

