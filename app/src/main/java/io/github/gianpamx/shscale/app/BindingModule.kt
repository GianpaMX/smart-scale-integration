package io.github.gianpamx.shscale.app

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import io.github.gianpamx.shscale.settings.SettingsViewModel

@Module
abstract class BindingModule {
    @Binds
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}
