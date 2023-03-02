package com.telestapp.rateexchange.fragments.rates.models

import com.telestapp.rateexchange.core.data.CurrencyInfo

sealed interface RatesEvent {
    object OnStart : RatesEvent

    data class OnEnterText(val text: String) : RatesEvent

    object OnSelectClick : RatesEvent

    object OnSelectDismiss : RatesEvent

    data class OnSelectCurrency(val currencyInfo: CurrencyInfo): RatesEvent
}