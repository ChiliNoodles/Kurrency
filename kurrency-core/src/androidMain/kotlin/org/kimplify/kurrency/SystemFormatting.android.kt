package org.kimplify.kurrency

import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Android implementation of SystemFormatting.
 * Uses system default locale formatting.
 */
class SystemFormatting(val locale: Locale): ISystemFormatting {
    override val decimalSeparator: String
        get() = DecimalFormatSymbols.getInstance(locale).decimalSeparator.toString()

    override val groupingSeparator: String
        get() = DecimalFormatSymbols.getInstance(locale).groupingSeparator.toString()
}
