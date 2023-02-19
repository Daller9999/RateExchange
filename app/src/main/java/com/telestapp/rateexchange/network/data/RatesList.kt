package com.telestapp.rateexchange.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatesTypedToken(
    @SerialName("base")
    val base: String,
    @SerialName("rates")
    val rates: Map<String, Double>
)
