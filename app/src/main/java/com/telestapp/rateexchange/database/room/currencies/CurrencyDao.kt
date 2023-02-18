package com.telestapp.rateexchange.database.room.currencies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CurrencyDao {

    @Query("select * from CurrencyTable")
    fun getAllCurrencies(): List<CurrencyEntity>

    @Insert
    fun insetCurrency(currency: CurrencyEntity)

}