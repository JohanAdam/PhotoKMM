package com.nyan.photokmm.android

import android.app.Application
import com.nyan.photokmm.android.di.appModule
import com.nyan.photokmm.di.getSharedModules
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule + getSharedModules())
        }
    }

}