package com.example.lyndenflood_comp304_lab2_ex1

import android.app.Application
import com.example.lyndenflood_comp304_lab2_ex1.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Koin or any other dependency injection framework here
        // For example, if using Koin:
        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}