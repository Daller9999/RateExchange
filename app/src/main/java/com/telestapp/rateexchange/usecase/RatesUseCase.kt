package com.telestapp.rateexchange.usecase

import com.telestapp.rateexchange.core.data.CurrencyInfo
import com.telestapp.rateexchange.core.data.RatesListInfo
import com.telestapp.rateexchange.database.repository.CurrencyRepository
import com.telestapp.rateexchange.database.repository.RatesRepository
import com.telestapp.rateexchange.network.RatesApi
import com.telestapp.rateexchange.preference.Preference

class RatesUseCase(
    private val ratesRepository: RatesRepository,
    private val currencyRepository: CurrencyRepository,
    private val ratesApi: RatesApi,
    private val preference: Preference
) {

    companion object {
        private const val min30 = 30 * 60 * 1000
    }
    suspend fun getAllCurrencies(): List<CurrencyInfo> {
        var currencies = currencyRepository.getAllCurrencies()
        val currentTime = System.currentTimeMillis()
        if (currencies.isEmpty() || (currentTime - preference.lastCallCurrency) > min30) {
            currencies = ratesApi.getCurrencyList()
            currencyRepository.clearTable()
            currencies.forEach {
                currencyRepository.insertCurrency(it)
            }
            preference.lastCallCurrency = currentTime
        }
        return currencies
    }

    suspend fun getCurrencyRatesByName(currency: String): RatesListInfo {
        var rates = ratesRepository.getRateExchangeByName(currency)
        val currentTime = System.currentTimeMillis()
        if (rates.exchanges.isEmpty() || (currentTime - preference.lastCallRate) > min30) {
            ratesRepository.clearTable()
            rates = ratesApi.getExchangeRatesList(currency)
            ratesRepository.insertRate(rates)
            preference.lastCallRate = currentTime
        }
        return rates
    }

}