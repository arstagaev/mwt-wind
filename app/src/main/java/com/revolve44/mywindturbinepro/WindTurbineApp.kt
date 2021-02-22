package com.revolve44.mywindturbinepro

import android.app.Application
import com.revolve44.mywindturbinepro.storage.PreferenceMaestro
import timber.log.Timber

class WindTurbineApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // init SharedPref
        PreferenceMaestro.init(this)

        // init Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.tag("ars")
        }

        //dummySettings()
    }
}