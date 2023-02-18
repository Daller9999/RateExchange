package com.telestapp.rateexchange.database.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.telestapp.rateexchange.core.data.ExchangeRate
import com.telestapp.rateexchange.core.data.RatesListInfo
import com.telestapp.rateexchange.database.room.exchanges.ExchangeDao
import com.telestapp.rateexchange.database.room.exchanges.ExchangeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RatesRepository(
    private val exchangeDao: ExchangeDao
) {

    private val dispatcher = Dispatchers.IO
    private val gson = Gson()

    companion object {
        private val typedValue = object : TypeToken<List<ExchangeRate>>() {}.type
    }

    fun clearTable() = exchangeDao.clearTable()

    suspend fun insertRate(rate: RatesListInfo) = withContext(dispatcher) {
        exchangeDao.addExchange(rate.toRatesEntity())
    }

    suspend fun getRateExchangeByName(currency: String) = withContext(dispatcher) {
        return@withContext exchangeDao.getCurrencyByName(currency)?.toRatesListInfo() ?: RatesListInfo()
    }

    private fun RatesListInfo.toRatesEntity(): ExchangeEntity {
        return ExchangeEntity(
            currency = currency,
            exchanges = gson.toJson(exchanges)
        )
    }

    private fun ExchangeEntity.toRatesListInfo(): RatesListInfo {
        val exchangeList: List<ExchangeRate> = try {
            gson.fromJson(exchanges, typedValue)
        } catch (ex: Exception) {
            emptyList()
        }
        return RatesListInfo(
            currency = currency,
            exchanges = exchangeList
        )
    }

}