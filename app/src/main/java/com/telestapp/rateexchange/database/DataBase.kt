package com.telestapp.rateexchange.database

import android.app.Application
import androidx.room.*
import com.telestapp.rateexchange.database.room.currencies.CurrencyDao
import com.telestapp.rateexchange.database.room.currencies.CurrencyEntity
import com.telestapp.rateexchange.database.room.exchanges.ExchangeDao
import com.telestapp.rateexchange.database.room.exchanges.ExchangeEntity

//@TypeConverters(TypeConverter::class)
@Database(entities = [ExchangeEntity::class, CurrencyEntity::class], version = 1)
abstract class DataBase : RoomDatabase() {
    companion object {
        fun buildDataBase(application: Application): DataBase {
            return Room.databaseBuilder(
                application.applicationContext,
                DataBase::class.java,
                "DataBaseExchanges"
            ).build()
        }
    }

    abstract fun getCurrencyDao(): CurrencyDao

    abstract fun getExchangeDao(): ExchangeDao
}