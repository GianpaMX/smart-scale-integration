package io.github.gianpamx.shscale.settings

import io.github.gianpamx.shscale.domain.ObserveSettings
import io.github.gianpamx.shscale.domain.SaveSettings
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.github.gianpamx.shscale.domain.entity.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    observeSettings: ObserveSettings,
    private val saveSettings: SaveSettings,
    private val errorChannel: BroadcastChannel<Throwable>,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    val state: LiveData<SettingsState> = observeSettings()
        .map { it.toViewState() }
        .flowOn(defaultDispatcher)
        .catch { errorChannel.send(it) }
        .asLiveData(viewModelScope.coroutineContext)

    fun onRunInTheBackgroundChange(newValue: Boolean) {
        val settingsState = state.value ?: SettingsState()
        saveSettings(settingsState.copy(runInTheBackground = newValue).toEntity())
    }

    private fun Settings.toViewState() = SettingsState(
        runInTheBackground = runInTheBackground
    )

    private fun SettingsState.toEntity() = Settings(
        runInTheBackground = runInTheBackground
    )
}
