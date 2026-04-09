package com.moisesai

import android.app.Application
import com.moisesai.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoisesAiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@MoisesAiApplication.applicationContext)
            modules(AppModule.list)
        }
    }
}
