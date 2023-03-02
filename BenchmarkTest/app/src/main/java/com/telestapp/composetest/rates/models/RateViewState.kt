package com.telestapp.composetest.rates.models

import com.telestapp.composetest.core.data.CurrencyInfo
import com.telestapp.composetest.rates.data.ExchangeInfo

data class RateViewState(
    val isVisibleSelectDialog: Boolean = false,
    val enterText: String = "",
    val selectedCurrency: CurrencyInfo = CurrencyInfo("", ""),
    val rates: List<ExchangeInfo> = emptyList(),
    val currencies: List<CurrencyInfo> = emptyList()
)