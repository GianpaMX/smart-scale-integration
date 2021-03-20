package io.github.gianpamx.shscale.app

import android.app.Application

class TestApp : Application(), ComponentApp {
    override val component: AppComponent by lazy { DaggerTestComponent.builder().application(this).build() }
}
