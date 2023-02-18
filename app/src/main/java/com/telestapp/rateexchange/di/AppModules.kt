package com.telestapp.rateexchange.di

import com.telestapp.rateexchange.database.DataBase
import com.telestapp.rateexchange.database.repository.CurrencyRepository
import com.telestapp.rateexchange.database.repository.RatesRepository
import com.telestapp.rateexchange.database.room.exchanges.ExchangeDao
import com.telestapp.rateexchange.fragments.rates.RatesViewModel
import com.telestapp.rateexchange.network.RatesApi
import com.telestapp.rateexchange.preference.Preference
import com.telestapp.rateexchange.usecase.RatesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val database = module {
    single { DataBase.buildDataBase(get()) }

    fun provideExchangeDao(dbMain: DataBase) = dbMain.getExchangeDao()
    fun provideCurrencyDao(dbMain: DataBase) = dbMain.getCurrencyDao()

    single { provideExchangeDao(get()) }
    single { provideCurrencyDao(get()) }

    single { RatesRepository(get()) }
    single { CurrencyRepository(get()) }
}

val network = module {
    single { RatesApi() }
}


val viewModels = module {
    viewModel { RatesViewModel(get()) }
}

val useCase = module {
    factory { RatesUseCase(get(), get(), get(), get()) }
}

val preference = module {
    single { Preference(get()) }
}

val modulesList = database + network + viewModels + useCase + preference