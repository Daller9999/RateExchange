package com.telestapp.rateexchange.di

import com.telestapp.rateexchange.database.DataBase
import com.telestapp.rateexchange.database.repository.RatesRepository
import com.telestapp.rateexchange.database.room.ExchangeDao
import com.telestapp.rateexchange.fragments.rates.RatesViewModel
import com.telestapp.rateexchange.network.RatesApi
import com.telestapp.rateexchange.preferences.RatesPreferences
import com.telestapp.rateexchange.usecase.RatesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.math.sin

val preferences = module {
    single { RatesPreferences(get()) }
}

val database = module {
    single { DataBase.buildDataBase(get()) }

    fun provideDao(dbMain: DataBase): ExchangeDao {
        return dbMain.getMovieDao()
    }

    single { provideDao(get()) }

    single { RatesRepository(get()) }
}

val network = module {
    single { RatesApi() }
}


val viewModels = module {
    viewModel { RatesViewModel(get()) }
}

val useCase = module {
    factory { RatesUseCase(get(), get(), get()) }
}

val modulesList = preferences + database + network + viewModels + useCase