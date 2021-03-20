package io.github.gianpamx.shscale.app

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree


interface ComponentApp {
    val component: AppComponent
}

class App : Application(), ComponentApp {
    override val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(DebugTree())

        component.inject(this)
    }
}
