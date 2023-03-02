package com.telestapp.rateexchange.database.room.currencies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CurrencyTable")
data class CurrencyEntity(
    val shortName: String,
    val longName: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)