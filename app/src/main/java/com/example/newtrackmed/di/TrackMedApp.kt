package com.example.newtrackmed.di

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class  TrackMedApp: Application(){
    private val applicationScope = CoroutineScope(SupervisorJob())


    companion object{
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this, applicationScope)
    }
}