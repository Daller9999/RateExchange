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
import java.math.BigDecimal

class RatesViewModel(
    private val ratesUseCase: RatesUseCase
) : BaseViewModel<RateViewState, RatesEvent>(RateViewState()) {

    companion object {
        private const val DEFAULT_CURRENCY = "USD"
        private const val ROUND_SCALE = 2
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
        calculateCurrency(text.toDoubleOrNull() ?: 0.0)
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
                exchangedRate = rate.roundString(),
                rate = (rate * count).roundString()
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

    private fun Double.roundString(): String {
        val normal = BigDecimal(this).toPlainString()
        val split = normal.split(".")
        if (split.size == 1) return normal.splitSpace()
        val part = split[1]
        for (i in part.indices) {
            if (part[i].toString() != "0") {
                return "${split[0].splitSpace()},${
                    part.substring(
                        0,
                        if (i + ROUND_SCALE >= part.length) part.length else i + ROUND_SCALE
                    )
                }"
            }
        }
        return normal.splitSpace()
    }

    private fun String.splitSpace(): String {
        val string = StringBuilder()
        var count = 1
        for (i in length - 1 downTo 0) {
            string.append(this[i].toString())
            if (count % 3 == 0 && count > 0 && count <= length - 1) {
                string.append(".")
            }
            count++
        }
        return string.toString().reversed()
    }
}