package io.github.gianpamx.shscale.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import io.github.gianpamx.shscale.domain.api.StorageApi
import io.github.gianpamx.shscale.domain.entity.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

private const val BG_KEY = "runInTheBackground"

class SharedPreferencesStorageApi(private val sharedPreferences: SharedPreferences) : StorageApi {
    private val settingsFlow: MutableStateFlow<Settings> = MutableStateFlow(settings)

    override var settings: Settings
        get() = Settings(
            runInTheBackground = sharedPreferences.getBoolean(BG_KEY, false)
        )
        set(value) {
            sharedPreferences.edit {
                putBoolean(BG_KEY, value.runInTheBackground)
            }
            settingsFlow.value = value
        }

    override fun liveSettings(): Flow<Settings> = settingsFlow
}
