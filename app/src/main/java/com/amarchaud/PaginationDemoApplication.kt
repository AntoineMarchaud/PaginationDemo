package com.amarchaud

import android.app.Application
import com.amarchaud.data.di.daoModule
import com.amarchaud.data.di.networkModule
import com.amarchaud.data.di.repoModule
import com.amarchaud.data.di.useCaseModule
import com.amarchaud.ui.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger

class PaginationDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            logger(PrintLogger(Level.DEBUG))
            androidContext(this@PaginationDemoApplication)
            modules(networkModule, daoModule, repoModule, useCaseModule, viewModelModule)
        }
    }
}