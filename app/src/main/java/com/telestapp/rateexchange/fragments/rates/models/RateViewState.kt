package com.telestapp.rateexchange.fragments.rates.models

data class RateViewState(
    val currency: String = "",
    val list: List<RateInfo> = emptyList()
)

data class RateInfo(
    val currency: String,
    val rate: Float
)