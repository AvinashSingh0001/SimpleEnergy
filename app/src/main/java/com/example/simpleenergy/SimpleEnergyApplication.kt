package com.example.simpleenergy

import android.app.Application
import com.example.simpleenergy.di.AppContainer

class SimpleEnergyApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
