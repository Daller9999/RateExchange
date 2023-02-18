package com.telestapp.rateexchange.network.data

import com.google.gson.reflect.TypeToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val ratesTypedValue = object : TypeToken<RatesTypedToken>() {}.type

@Serializable
data class RatesTypedToken(
    @SerialName("base")
    val base: String,
    @SerialName("rates")
    val rates: Map<String, Double>
)
