package io.github.gianpamx.shscale.domain

import io.github.gianpamx.shscale.domain.api.StorageApi
import io.github.gianpamx.shscale.domain.entity.Settings
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SaveSettingsTest {
    private val storageApi = object : StorageApi {
        override var settings: Settings = Settings(
            isBackgroundServiceEnabled = true
        )

        override fun liveSettings() = TODO("Not needed")
    }

    @Test
    fun `Save Settings`() {
        val saveSettings = SaveSettings(storageApi)

        saveSettings(Settings(isBackgroundServiceEnabled = false))

        assertThat(storageApi.settings.isBackgroundServiceEnabled).isFalse()
    }
}
