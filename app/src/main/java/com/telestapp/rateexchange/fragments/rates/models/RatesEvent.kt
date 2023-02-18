package com.telestapp.rateexchange.fragments.rates.models

sealed interface RatesEvent {
    object OnStart: RatesEvent

    data class OnEnterText(val text: String): RatesEvent
}