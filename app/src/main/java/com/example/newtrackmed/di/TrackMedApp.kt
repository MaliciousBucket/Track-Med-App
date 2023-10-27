package com.example.newtrackmed.di

import android.app.Application

class  TrackMedApp: Application(){

    companion object{
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}