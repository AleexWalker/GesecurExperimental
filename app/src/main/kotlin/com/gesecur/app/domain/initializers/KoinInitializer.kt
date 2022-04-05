package com.gesecur.app.domain.initializers

import android.content.Context
import androidx.startup.Initializer
import com.gesecur.app.di.DataModuleProvider
import com.gesecur.app.di.NetworkModuleProvider
import com.gesecur.app.di.ViewModelsModuleProvider
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class KoinInitializer : Initializer<KoinApplication> {

    override fun create(context: Context): KoinApplication {
        val modules = mutableListOf<Module>().apply {
            add(DataModuleProvider.getModule())
            add(ViewModelsModuleProvider.getModule())
            add(NetworkModuleProvider.getModule())
        }

        return startKoin {
            androidLogger()
            androidContext(context)
            modules(modules)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}