package com.chilinoodles.kurrency.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chilinoodles.kurrency.rememberCurrencyState

@Composable
fun QuickStartSample() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SimpleCurrencyDisplay()
        }
    }
}

@Composable
fun SimpleCurrencyDisplay() {
    val currencyState = rememberCurrencyState(
        currencyCode = "USD",
        initialAmount = "1234.56"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Balance",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        
        Text(
            text = currencyState.formattedAmount,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = currencyState.formattedAmountIso,
            fontSize = 20.sp
        )
        
        Button(
            onClick = { currencyState.updateAmount("5000.00") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update to $5000")
        }
        
        Button(
            onClick = { currencyState.updateCurrency("EUR") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Switch to EUR")
        }
    }
}

@Composable
fun PriceTagExample() {
    val price = rememberCurrencyState("USD", "29.99")
    
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = "Price:",
            fontSize = 14.sp
        )
        Text(
            text = price.formattedAmount,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MultiCurrencyPriceDisplay(baseAmount: String) {
    val usdPrice = rememberCurrencyState("USD", baseAmount)
    val eurPrice = rememberCurrencyState("EUR", baseAmount)
    val gbpPrice = rememberCurrencyState("GBP", baseAmount)
    
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("USD: ${usdPrice.formattedAmount}")
        Text("EUR: ${eurPrice.formattedAmount}")
        Text("GBP: ${gbpPrice.formattedAmount}")
    }
}

