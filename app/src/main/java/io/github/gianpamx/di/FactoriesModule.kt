package io.github.gianpamx.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class FactoriesModule {
    @Provides
    fun provideViewModelFactory(modelProvider: Provider<ViewModel>): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = modelProvider.get() as T
        }
}
