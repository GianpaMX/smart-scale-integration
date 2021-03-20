package io.github.gianpamx.shscale.app

import android.app.Application
import dagger.Module
import dagger.Provides
import io.github.gianpamx.di.AppScope

@Module
class TestModule {
    @Provides
    @AppScope
    fun provideApplication(testApp: TestApp): Application = testApp
}
