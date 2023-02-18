package com.telestapp.rateexchange

import android.app.Application
import com.telestapp.rateexchange.di.modulesList
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RatesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RatesApplication)
            modules(modulesList)
        }
    }
}