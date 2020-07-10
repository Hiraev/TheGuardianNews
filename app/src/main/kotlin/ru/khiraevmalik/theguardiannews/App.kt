package ru.khiraevmalik.theguardiannews

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.khiraevmalik.theguardiannews.koin.navigationModule
import ru.khiraevmalik.theguardiannews.koin.viewModelsModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(navigationModule, viewModelsModule)
        }
    }

}
