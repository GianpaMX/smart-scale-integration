package io.github.gianpamx.shscale.app

import android.app.Application
import dagger.Module
import dagger.Provides
import io.github.gianpamx.di.AppScope
import io.github.gianpamx.shscale.domain.api.StorageApi

@Module
class TestModule {
    @Provides
    @AppScope
    fun provideApplication(testApp: TestApp): Application = testApp

    @Provides
    @AppScope
    fun provideStorageApi(): StorageApi = TODO("Not needed")
}
