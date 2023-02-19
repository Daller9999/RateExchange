package com.telestapp.rateexchange.network

import com.telestapp.rateexchange.BuildConfig
import com.telestapp.rateexchange.core.data.CurrencyInfo
import com.telestapp.rateexchange.core.data.ExchangeRate
import com.telestapp.rateexchange.core.data.RatesListInfo
import com.telestapp.rateexchange.network.data.RatesTypedToken
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class RatesApi {

    companion object {
        private const val BASE_URL = "https://openexchangerates.org/api"
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
        val result: Map<String, String> = client.request(url) {
            parameter("app_id", BuildConfig.RATE_API_KEY)
        }
        return try {
            return result.keys.toList().map {
                CurrencyInfo(
                    shortName = it,
                    longName = result[it] ?: ""
                )
            }
        } catch (ex: Exception) {
            emptyList()
        }
    }

    suspend fun getExchangeRatesList(currency: String): RatesListInfo {
        val url = "$BASE_URL/latest.json"
        val result: RatesTypedToken = client.request(url) {
            method = HttpMethod.Get
            parameter("base", currency)
            parameter("app_id", BuildConfig.RATE_API_KEY)
        }
        return try {
            val arrayList = arrayListOf<ExchangeRate>()
            for (key in result.rates.keys) {
                arrayList.add(
                    ExchangeRate(
                        currency = key,
                        rate = result.rates[key] ?: 0.0
                    )
                )
            }
            RatesListInfo(
                currency = result.base,
                exchanges = arrayList
            )
        } catch (ex: Exception) {
            RatesListInfo()
        }
    }
}