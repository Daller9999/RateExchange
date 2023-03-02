package com.telestapp.composetest.usecase

import com.telestapp.composetest.core.data.CurrencyInfo
import com.telestapp.composetest.core.data.ExchangeRate
import com.telestapp.composetest.core.data.RatesListInfo
import kotlin.random.Random

class RatesUseCase {


    suspend fun getAllCurrencies(): List<CurrencyInfo> {
        val currencies = arrayListOf<CurrencyInfo>()
        for (i in 0 until 1000) {
            currencies.add(CurrencyInfo("U${0}${i + 2}", "U${0}${i + 2}${i - 1}${i}"))
        }
        return currencies
    }

    suspend fun getCurrencyRatesByName(currency: String): RatesListInfo {
        val exchanges = arrayListOf<ExchangeRate>()
        for (i in 0 until 2000) {
            exchanges.add(ExchangeRate("US$i", Random.nextInt(10, 100).toDouble()))
        }
        return RatesListInfo(currency, exchanges)
    }

}