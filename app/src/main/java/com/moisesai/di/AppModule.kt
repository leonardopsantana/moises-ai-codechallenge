package com.moisesai.di

import android.content.res.Resources
import com.moisesai.common.qualifier.QualifierDispatcherIO
import com.moisesai.networking.di.NetworkModule
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.collections.plus

object AppModule {
    private val module = module {
        single<Resources> { androidContext().resources }
        single(QualifierDispatcherIO) { Dispatchers.IO }
    }
    val list  = module + NetworkModule.module
}