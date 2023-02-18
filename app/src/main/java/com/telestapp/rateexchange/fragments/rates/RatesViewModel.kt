package com.telestapp.rateexchange.fragments.rates

import com.telestapp.rateexchange.core.BaseViewModel
import com.telestapp.rateexchange.fragments.rates.models.RateViewState
import com.telestapp.rateexchange.fragments.rates.models.RatesEvent
import com.telestapp.rateexchange.usecase.RatesUseCase

class RatesViewModel(
    private val ratesUseCase: RatesUseCase
) : BaseViewModel<RateViewState, RatesEvent>(RateViewState()) {

    override fun obtainEvent(viewEvent: RatesEvent) {

    }
}