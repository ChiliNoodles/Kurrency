package org.kimplify.kurrency

import platform.Foundation.NSLocale
import platform.Foundation.NSLocaleDecimalSeparator
import platform.Foundation.NSLocaleGroupingSeparator
import platform.Foundation.currentLocale

/**
 * iOS implementation of SystemFormatting.
 * Uses NSLocale.currentLocale to read formatting preferences configured by the user.
 */
object SystemFormatting : ISystemFormatting {
    override val decimalSeparator: String
        get() = (NSLocale.currentLocale.objectForKey(NSLocaleDecimalSeparator) as? String)
            ?.firstOrNull()?.toString() ?: "."

    override val groupingSeparator: String
        get() = (NSLocale.currentLocale.objectForKey(NSLocaleGroupingSeparator) as? String)
            ?.firstOrNull()?.toString() ?: ","
}
