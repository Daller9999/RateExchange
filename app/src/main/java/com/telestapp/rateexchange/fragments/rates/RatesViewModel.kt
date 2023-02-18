package com.telestapp.rateexchange.fragments.rates

import com.telestapp.rateexchange.core.BaseViewModel
import com.telestapp.rateexchange.core.data.CurrencyInfo
import com.telestapp.rateexchange.core.data.ExchangeRate
import com.telestapp.rateexchange.core.data.RatesListInfo
import com.telestapp.rateexchange.fragments.rates.data.ExchangeInfo
import com.telestapp.rateexchange.fragments.rates.models.RateViewState
import com.telestapp.rateexchange.fragments.rates.models.RatesEvent
import com.telestapp.rateexchange.usecase.RatesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RatesViewModel(
    private val ratesUseCase: RatesUseCase
) : BaseViewModel<RateViewState, RatesEvent>(RateViewState()) {

    companion object {
        private const val DEFAULT_CURRENCY = "USD"
        private const val ROUND_SCALE = 10
    }

    private var ratesListInfo = RatesListInfo()
    private var calculateRates = RatesListInfo()
    override fun obtainEvent(viewEvent: RatesEvent) {
        when (viewEvent) {
            is RatesEvent.OnStart -> onStart()
            is RatesEvent.OnEnterText -> onEnterText(viewEvent.text)
            RatesEvent.OnSelectClick -> onSelectClick()
            RatesEvent.OnSelectDismiss -> onSelectDismiss()
            is RatesEvent.OnSelectCurrency -> onSelectCurrency(viewEvent.currencyInfo)
        }
    }

    private fun onSelectCurrency(currencyInfo: CurrencyInfo) {
        update {
            it.copy(
                isVisibleSelectDialog = false,
                selectedCurrency = currencyInfo
            )
        }
        calculateRates(currencyInfo)
    }

    private fun onSelectDismiss() {
        update { it.copy(isVisibleSelectDialog = false) }
    }

    private fun onSelectClick() {
        update { it.copy(isVisibleSelectDialog = true) }
    }

    private fun onEnterText(text: String) = scopeIO.launch {
        update { it.copy(enterText = text) }
        text.toDoubleOrNull()?.let {
            calculateCurrency(it)
        }
    }

    private fun onStart() = scopeIO.launch {
        val currencies = ratesUseCase.getAllCurrencies()
        update { state ->
            state.copy(
                currencies = currencies,
                selectedCurrency = currencies.firstOrNull { it.shortName == "USD" } ?: currencies[0]
            )
        }
        requestCurrency()
    }

    private suspend fun requestCurrency() = with(viewStates().value) {
        val count = enterText.toDoubleOrNull() ?: 0.0
        val result = ratesUseCase.getCurrencyRatesByName(DEFAULT_CURRENCY)
        ratesListInfo = result
        calculateRates = result
        calculateCurrency(count)
    }

    private suspend fun calculateCurrency(count: Double) = withContext(Dispatchers.IO) {
        var rate: Double
        val arrayList = calculateRates.exchanges.map {
            rate = it.rate
            ExchangeInfo(
                currency = it.currency,
                exchangedRate = rate.toString().roundString(),
                rate = (rate * count).toString().roundString()
            )
        }
        update { it.copy(rates = arrayList) }
    }

    private fun calculateRates(currency: CurrencyInfo) = scopeIO.launch {
        val arrayList = arrayListOf<ExchangeRate>()
        val rate = ratesListInfo.exchanges.firstOrNull {
            it.currency == currency.shortName
        } ?: return@launch
        calculateRates = if (currency.shortName != DEFAULT_CURRENCY) {
            ratesListInfo.exchanges.forEach { item ->
                arrayList.add(
                    ExchangeRate(
                        currency = item.currency,
                        rate = item.rate / rate.rate
                    )
                )
            }
            calculateRates.copy(exchanges = arrayList)
        } else {
            calculateRates.copy(exchanges = ratesListInfo.exchanges)
        }
        calculateCurrency(1.0)
    }

    private fun String.roundString(): String {
        val split = split(".")
        if (split.size == 1) return this
        val part = split[1]
        for (i in 1 until part.length) {
            if (part[i].toString() != "0") {
                return "${split[0]}.${part.substring(0, if (i + 2 >= part.length) part.length else i + 2)}"
            }
        }
        return this
    }
}