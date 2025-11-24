package org.kimplify.kurrency

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CurrencyTest {
    
    @Test
    fun testCurrencyCreation() {
        val currency = Currency("USD")
        assertEquals("USD", currency.code)
        assertEquals(2, currency.fractionDigits)
    }
    
    @Test
    fun testJapaneseCurrencyFractionDigits() {
        val currency = Currency("JPY")
        assertEquals("JPY", currency.code)
        assertEquals(0, currency.fractionDigits)
    }
    
    @Test
    fun testFormatAmountReturnsSuccess() {
        val currency = Currency("USD")
        val result = currency.formatAmount("100.50")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testFormatAmountWithDoubleReturnsSuccess() {
        val currency = Currency("USD")
        val result = currency.formatAmount(100.50)
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testFormatAmountStandardStyle() {
        val currency = Currency("USD")
        val result = currency.formatAmount("1234.56", CurrencyStyle.Standard)
        
        assertTrue(result.isSuccess)
        val formatted = result.getOrNull()
        assertNotNull(formatted)
    }
    
    @Test
    fun testFormatAmountIsoStyle() {
        val currency = Currency("USD")
        val result = currency.formatAmount("1234.56", CurrencyStyle.Iso)
        
        assertTrue(result.isSuccess)
        val formatted = result.getOrNull()
        assertNotNull(formatted)
    }
    
    @Test
    fun testFormatAmountWithInvalidAmount() {
        val currency = Currency("USD")
        val result = currency.formatAmount("invalid")
        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is KurrencyError.InvalidAmount)
    }
    
    @Test
    fun testFormatAmountWithEmptyString() {
        val currency = Currency("USD")
        val result = currency.formatAmount("")
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is KurrencyError.InvalidAmount)
    }
    
    @Test
    fun testFormatAmountWithInvalidCurrencyCode() {
        val currency = Currency("INVALID")
        val result = currency.formatAmount("100.00")
        
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is KurrencyError.InvalidCurrencyCode)
    }
    
    @Test
    fun testFormatAmountWithShortCurrencyCode() {
        val currency = Currency("US")
        val result = currency.formatAmount("100.00")
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is KurrencyError.InvalidCurrencyCode)
    }
    
    @Test
    fun testFormatAmountWithLongCurrencyCode() {
        val currency = Currency("USDD")
        val result = currency.formatAmount("100.00")
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is KurrencyError.InvalidCurrencyCode)
    }
    
    @Test
    fun testFormatAmountOrEmpty() {
        val currency = Currency("USD")
        val formatted = currency.formatAmountOrEmpty("100.50")
        
        assertNotNull(formatted)
        assertFalse(formatted.isEmpty())
    }
    
    @Test
    fun testFormatAmountOrEmptyWithInvalidAmount() {
        val currency = Currency("USD")
        val formatted = currency.formatAmountOrEmpty("invalid")
        
        assertEquals("", formatted)
    }
    
    @Test
    fun testFormatAmountWithZero() {
        val currency = Currency("USD")
        val result = currency.formatAmount("0.00")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testFormatAmountWithNegativeNumber() {
        val currency = Currency("USD")
        val result = currency.formatAmount("-100.50")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testFormatAmountWithVeryLargeNumber() {
        val currency = Currency("USD")
        val result = currency.formatAmount("999999999.99")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testFormatAmountWithCommaDecimalSeparator() {
        val currency = Currency("EUR")
        val result = currency.formatAmount("100,50")
        
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun testPropertyDelegation() {
        val currency = Currency("USD")
        val delegate = currency.format("100.50")
        
        assertNotNull(delegate)
    }
    
    @Test
    fun testPropertyDelegationWithStyle() {
        val currency = Currency("USD")
        val delegate = currency.format("100.50", CurrencyStyle.Iso)
        
        assertNotNull(delegate)
    }
    
    @Test
    fun testEuroFormatting() {
        val currency = Currency("EUR")
        val result = currency.formatAmount("1234.56")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testBritishPoundFormatting() {
        val currency = Currency("GBP")
        val result = currency.formatAmount("1234.56")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testJapaneseYenFormatting() {
        val currency = Currency("JPY")
        val result = currency.formatAmount("1234")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testSwissFrancFormatting() {
        val currency = Currency("CHF")
        val result = currency.formatAmount("1234.56")
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
    
    @Test
    fun testCurrencyEquality() {
        val currency1 = Currency("USD")
        val currency2 = Currency("USD")
        
        assertEquals(currency1, currency2)
        assertTrue(currency1 == currency2)
    }
    
    @Test
    fun testCurrencyInequality() {
        val currency1 = Currency("USD")
        val currency2 = Currency("EUR")
        
        assertFalse(currency1 == currency2)
        assertTrue(currency1 != currency2)
    }
    
    @Test
    fun testCurrencyEqualityWithDifferentFractionDigits() {
        val currency1 = Currency("USD", 2)
        val currency2 = Currency("USD", 3)
        
        assertEquals(currency1, currency2)
        assertTrue(currency1 == currency2)
    }
    
    @Test
    fun testCurrencyHashCode() {
        val currency1 = Currency("USD")
        val currency2 = Currency("USD")
        
        assertEquals(currency1.hashCode(), currency2.hashCode())
    }
    
    @Test
    fun testCurrencyHashCodeDifferentCodes() {
        val currency1 = Currency("USD")
        val currency2 = Currency("EUR")
        
        assertTrue(currency1.hashCode() != currency2.hashCode())
    }
    
    @Test
    fun testIsValidWithValidCurrency() {
        assertTrue(Currency.isValid("USD"))
        assertTrue(Currency.isValid("EUR"))
        assertTrue(Currency.isValid("GBP"))
        assertTrue(Currency.isValid("JPY"))
    }
    
    @Test
    fun testIsValidWithInvalidFormat() {
        assertFalse(Currency.isValid("US"))
        assertFalse(Currency.isValid("USDD"))
        assertFalse(Currency.isValid("123"))
        assertFalse(Currency.isValid("US$"))
    }
    
    @Test
    fun testIsValidWithInvalidCurrencyCode() {
        assertFalse(Currency.isValid("INVALID"))
        assertFalse(Currency.isValid("XYZ"))
    }
    
    @Test
    fun testIsValidWithEmptyString() {
        assertFalse(Currency.isValid(""))
    }
    
    @Test
    fun testIsValidWithLowercase() {
        assertTrue(Currency.isValid("usd"))
        assertTrue(Currency.isValid("eur"))
        assertTrue(Currency.isValid("jpy"))
    }
}

