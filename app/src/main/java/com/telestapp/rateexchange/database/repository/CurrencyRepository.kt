package com.telestapp.rateexchange.database.repository

import com.telestapp.rateexchange.core.data.CurrencyInfo
import com.telestapp.rateexchange.database.room.currencies.CurrencyDao
import com.telestapp.rateexchange.database.room.currencies.CurrencyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyRepository(
    private val currencyDao: CurrencyDao
) {

    private val dispatcher = Dispatchers.IO

    suspend fun insertCurrency(currency: CurrencyInfo) = withContext(dispatcher) {
        currencyDao.insetCurrency(
            CurrencyEntity(
                shortName = currency.shortName,
                longName = currency.longName
            )
        )
    }

    suspend fun getAllCurrencies(): List<CurrencyInfo> = withContext(dispatcher) {
        return@withContext currencyDao.getAllCurrencies().toListCurrencyInfo()
    }

    private fun List<CurrencyEntity>.toListCurrencyInfo(): List<CurrencyInfo> {
        return map {
            CurrencyInfo(
                shortName = it.shortName,
                longName = it.longName
            )
        }
    }

}