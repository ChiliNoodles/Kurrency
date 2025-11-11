package com.chilinoodles.kurrency

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
class CurrencyFormatterAndroidTest {

    @Test
    fun testFormatCurrencyStyleSuccess() {
        val result = CurrencyFormatter.formatCurrencyStyle("1234.56", "USD")
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun testFormatIsoCurrencyStyleSuccess() {
        val result = CurrencyFormatter.formatIsoCurrencyStyle("1234.56", "EUR")
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun testFormatWithCommaDecimalSeparator() {
        val result = CurrencyFormatter.formatCurrencyStyle("100,50", "EUR")
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun testFormatWithNegative() {
        val result = CurrencyFormatter.formatCurrencyStyle("-1234.56", "USD")
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun testFormatWithZero() {
        val result = CurrencyFormatter.formatCurrencyStyle("0", "USD")
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun testGetFractionDigits() {
        val result = CurrencyFormatter.getFractionDigits("USD")
        assertTrue(result.isSuccess)
        assertEquals(result.getOrDefault(0), 2)
    }

    @Test
    fun testMultipleCurrencies() {
        val currencies = listOf("USD", "EUR", "GBP", "JPY", "CHF")
        currencies.forEach { code ->
            val result = CurrencyFormatter.formatCurrencyStyle("1234.56", code)
            assertTrue(result.isSuccess, "Failed for currency: $code")
        }
    }
}

