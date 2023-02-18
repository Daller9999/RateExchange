package com.telestapp.rateexchange.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.telestapp.rateexchange.BuildConfig
import com.telestapp.rateexchange.core.data.CurrencyInfo
import com.telestapp.rateexchange.core.data.ExchangeRate
import com.telestapp.rateexchange.core.data.RatesListInfo
import com.telestapp.rateexchange.network.data.RatesTypedToken
import com.telestapp.rateexchange.network.data.ratesTypedValue
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class RatesApi {

    companion object {
        private const val BASE_URL = "https://openexchangerates.org/api"
        private val gson = Gson()
        private val jsonListType = object : TypeToken<Map<String, String>>() {}.type
    }

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    init {
        client.config {
            expectSuccess = true
            defaultRequest {
                host = BASE_URL
            }
        }
    }

    suspend fun getCurrencyList(): List<CurrencyInfo> {
        val url = "$BASE_URL/currencies.json"
        val result: String = client.request(url) {
            parameter("app_id", BuildConfig.RATE_API_KEY)
        }
        return try {
            val resultJson: Map<String, String> = gson.fromJson(result, jsonListType)
            return resultJson.keys.toList().map {
                CurrencyInfo(
                    shortName = it,
                    longName = resultJson[it] ?: ""
                )
            }
        } catch (ex: Exception) {
            emptyList()
        }
    }

    suspend fun getExchangeRatesList(currency: String): RatesListInfo {
        val url = "$BASE_URL/latest.json"
        val result: String = client.request(url) {
            method = HttpMethod.Get
            parameter("base", currency)
            parameter("app_id", BuildConfig.RATE_API_KEY)
        }
        return try {
            val resultJson: RatesTypedToken = gson.fromJson(result, ratesTypedValue)
            val arrayList = arrayListOf<ExchangeRate>()
            for (key in resultJson.rates.keys) {
                arrayList.add(
                    ExchangeRate(
                        currency = key,
                        rate = resultJson.rates[key] ?: 0f
                    )
                )
            }
            RatesListInfo(
                currency = resultJson.base,
                exchanges = arrayList
            )
        } catch (ex: Exception) {
            RatesListInfo()
        }
    }
}