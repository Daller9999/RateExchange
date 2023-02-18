package com.telestapp.rateexchange.fragments.rates

import android.util.Log
import com.telestapp.rateexchange.core.BaseViewModel
import com.telestapp.rateexchange.fragments.rates.models.RateViewState
import com.telestapp.rateexchange.fragments.rates.models.RatesEvent
import com.telestapp.rateexchange.usecase.RatesUseCase
import kotlinx.coroutines.launch

class RatesViewModel(
    private val ratesUseCase: RatesUseCase
) : BaseViewModel<RateViewState, RatesEvent>(RateViewState()) {

    override fun obtainEvent(viewEvent: RatesEvent) {
        when (viewEvent) {
            is RatesEvent.OnStart -> onStart()
            is RatesEvent.OnEnterText -> onEnterText(viewEvent.text)
        }
    }

    private fun onEnterText(text: String) {

    }

    private fun onStart() = scopeIO.launch {
        val all = ratesUseCase.getAllCurrencies()
        Log.i("test_app", all.toString())
        val rates = ratesUseCase.getCurrencyRatesByName("USD")
        Log.i("test_app", rates.toString())
    }
}