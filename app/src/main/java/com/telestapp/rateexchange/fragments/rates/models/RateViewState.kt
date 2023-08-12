package com.telestapp.rateexchange.fragments.rates.models

import com.telestapp.rateexchange.core.data.CurrencyInfo
import com.telestapp.rateexchange.fragments.rates.data.ExchangeInfo

data class RateViewState(
    val isVisibleSelectDialog: Boolean = false,
    val enterText: String = "",
    val selectedCurrency: CurrencyInfo = CurrencyInfo("", ""),
    val rates: List<ExchangeInfo> = emptyList(),
    val currencies: List<CurrencyInfo> = emptyList()
)