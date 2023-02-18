package com.telestapp.rateexchange.usecase

import com.telestapp.rateexchange.core.data.CurrencyInfo
import com.telestapp.rateexchange.core.data.RatesListInfo
import com.telestapp.rateexchange.database.repository.CurrencyRepository
import com.telestapp.rateexchange.database.repository.RatesRepository
import com.telestapp.rateexchange.network.RatesApi

class RatesUseCase(
    private val ratesRepository: RatesRepository,
    private val currencyRepository: CurrencyRepository,
    private val ratesApi: RatesApi
) {

    suspend fun getAllCurrencies(): List<CurrencyInfo> {
        var currencies = currencyRepository.getAllCurrencies()
        if (currencies.isEmpty()) {
            currencies = ratesApi.getCurrencyList()
            currencies.forEach {
                currencyRepository.insertCurrency(it)
            }
        }
        return currencies
    }

    suspend fun getCurrencyRatesByName(currency: String): RatesListInfo {
        var rates = ratesRepository.getRateExchangeByName(currency)
        if (rates.exchanges.isEmpty()) {
            rates = ratesApi.getExchangeRatesList(currency)
            ratesRepository.insertRate(rates)
        }
        return rates
    }

}