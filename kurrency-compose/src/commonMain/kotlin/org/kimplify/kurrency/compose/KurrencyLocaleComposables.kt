package org.kimplify.kurrency.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import org.kimplify.kurrency.KurrencyLocale

@Composable
fun Locale.Companion.toKurrencyLocale(): KurrencyLocale {
    return KurrencyLocale.current()
}