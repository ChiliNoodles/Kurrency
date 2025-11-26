package org.kimplify.kurrency

interface CurrencyFormat {
    /**
     * Gets the number of fraction digits for a currency code, returning a default value on error.
     * This is the recommended method for UI use cases.
     *
     * @param currencyCode The ISO 4217 currency code (e.g., "USD", "EUR")
     * @param default The default value to return on error (defaults to 2)
     * @return The number of fraction digits, or default if there's an error
     */
    fun getFractionDigitsOrDefault(currencyCode: String, default: Int = 2): Int

    /**
     * Formats an amount in currency style, returning original value on error.
     * This is the recommended method for UI display.
     *
     * @param amount The amount to format
     * @param currencyCode The ISO 4217 currency code (e.g., "USD", "EUR")
     * @return The formatted string, or original amount if formatting fails
     */
    fun formatCurrencyStyle(amount: String, currencyCode: String): String

    /**
     * Formats an amount using a [Kurrency] instance, returning original value on error.
     *
     * @param amount The amount to format
     * @param currency The [Kurrency] whose ISO code should be used
     * @return The formatted string, or original amount if formatting fails
     */
    fun formatCurrencyStyle(amount: String, currency: Kurrency): String =
        formatCurrencyStyle(amount, currency.code)

    /**
     * Formats an amount in ISO currency style, returning original value on error.
     * This is the recommended method for UI display.
     *
     * @param amount The amount to format
     * @param currencyCode The ISO 4217 currency code (e.g., "USD", "EUR")
     * @return The formatted string with ISO code, or original amount if formatting fails
     */
    fun formatIsoCurrencyStyle(amount: String, currencyCode: String): String

    /**
     * Formats an amount in ISO style using a [Kurrency] instance, returning original value on error.
     *
     * @param amount The amount to format
     * @param currency The [Kurrency] whose ISO code should be used
     * @return The formatted string with ISO code, or original amount if formatting fails
     */
    fun formatIsoCurrencyStyle(amount: String, currency: Kurrency): String =
        formatIsoCurrencyStyle(amount, currency.code)
}
