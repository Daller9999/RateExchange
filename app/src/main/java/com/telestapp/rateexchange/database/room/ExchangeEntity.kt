package com.telestapp.rateexchange.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ExchangeTable")
data class ExchangeEntity(
    val currency: String,
    val exchanges: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)