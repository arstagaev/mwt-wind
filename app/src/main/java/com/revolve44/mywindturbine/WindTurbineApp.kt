package com.revolve44.mywindturbine

import android.app.Application
import com.revolve44.mywindturbine.storage.PreferenceMaestro
import timber.log.Timber

class WindTurbineApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // init sharedpref
        PreferenceMaestro.init(this)

        // init timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.tag("ars")
        }

        dummySettings()
    }
}