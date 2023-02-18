package com.telestapp.rateexchange.database

import android.app.Application
import androidx.room.*
import com.telestapp.rateexchange.database.room.ExchangeDao
import com.telestapp.rateexchange.database.room.ExchangeEntity

@TypeConverters(TypeConverter::class)
@Database(entities = [ExchangeEntity::class], version = 1)
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

    abstract fun getMovieDao(): ExchangeDao
}