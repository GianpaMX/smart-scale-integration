package io.github.gianpamx.shscale.app

import dagger.Module
import dagger.Provides
import io.github.gianpamx.di.AppScope
import io.github.gianpamx.shscale.domain.ObserveSettings
import io.github.gianpamx.shscale.domain.SaveSettings
import io.github.gianpamx.shscale.domain.api.StorageApi

@Module
class DomainModule {
    @Provides
    @AppScope
    fun provideObserveSettings(storageApi: StorageApi) = ObserveSettings(storageApi)

    @Provides
    @AppScope
    fun provideSaveSettings(storageApi: StorageApi) = SaveSettings(storageApi)
}
