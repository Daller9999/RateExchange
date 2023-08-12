package com.telestapp.rateexchange.preference

import android.content.Context
import android.content.SharedPreferences

class Preference(
    context: Context
) {

    companion object {
        private const val RATES_KEY = "RATES_KEY"
        private const val LAST_CALL_CURRENCY = "LAST_CALL_CURRENCY"
        private const val LAST_CALL_RATE = "LAST_CALL_RATE"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(RATES_KEY, Context.MODE_PRIVATE)

    var lastCallCurrency: Long
        get() = sharedPreferences.getLong(LAST_CALL_CURRENCY, 0)
        set(value) = sharedPreferences.edit().putLong(LAST_CALL_CURRENCY, value).apply()

    var lastCallRate: Long
        get() = sharedPreferences.getLong(LAST_CALL_RATE, 0)
        set(value) = sharedPreferences.edit().putLong(LAST_CALL_RATE, value).apply()

}