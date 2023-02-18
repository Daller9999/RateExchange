package com.telestapp.rateexchange.database.room.exchanges

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExchangeDao {

    @Insert
    fun addExchange(exchange: ExchangeEntity)

    @Query("select * from ExchangeTable where currency = :currency")
    fun getCurrencyByName(currency: String): ExchangeEntity?


}
