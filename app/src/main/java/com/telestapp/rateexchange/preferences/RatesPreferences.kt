package com.telestapp.rateexchange.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RatesPreferences(
    context: Context
) {

    companion object {
        private const val RATES_KEY = "RATES_DATA_KEY"
        private const val RATES_LIST = "RATES_LIST"
        private val jsonType = object : TypeToken<List<String>>() {}.type
    }

    private val gson = Gson()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(RATES_KEY, Context.MODE_PRIVATE)

    var ratesList: List<String> = emptyList()
        get() {
            val json = sharedPreferences.getString(RATES_LIST, "") ?: ""
            if (json.isEmpty()) return emptyList()
            return try {
                gson.fromJson(json, jsonType)
            } catch (ex: Exception) {
                emptyList()
            }
        }
        set(value) {
            val string = gson.toJson(value)
            sharedPreferences.edit().putString(RATES_LIST, string).apply()
            field = value
        }

}