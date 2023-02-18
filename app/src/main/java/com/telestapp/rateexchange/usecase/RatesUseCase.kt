package com.telestapp.rateexchange.usecase

import com.telestapp.rateexchange.database.repository.RatesRepository
import com.telestapp.rateexchange.network.RatesApi
import com.telestapp.rateexchange.preferences.RatesPreferences

class RatesUseCase(
    private val ratesRepository: RatesRepository,
    private val ratesApi: RatesApi,
    private val ratesPreferences: RatesPreferences
) {
}