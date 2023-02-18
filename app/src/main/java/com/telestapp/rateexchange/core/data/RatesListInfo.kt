package com.telestapp.rateexchange.core.data

data class RatesListInfo(
    val currency: String = "NONE",
    val exchanges: List<ExchangeRate> = emptyList()
)

data class ExchangeRate(
    val currency: String,
    val rate: Float
)