package io.github.gianpamx.shscale.app

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.github.gianpamx.di.AppScope
import io.github.gianpamx.shscale.domain.api.StorageApi
import io.github.gianpamx.shscale.storage.SharedPreferencesStorageApi

@Module
class ApiModule {
    @Provides
    @AppScope
    fun provideStorageApi(sharedPreferences: SharedPreferences): StorageApi =
        SharedPreferencesStorageApi(sharedPreferences)
}
