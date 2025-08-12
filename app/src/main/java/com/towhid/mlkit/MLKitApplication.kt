package com.towhid.mlkit

import android.app.Application
import com.towhid.mlkit.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MLKitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MLKitApplication)
            modules(appModule)
        }
    }
}