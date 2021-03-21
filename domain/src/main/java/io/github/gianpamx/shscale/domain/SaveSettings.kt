package io.github.gianpamx.shscale.domain

import io.github.gianpamx.shscale.domain.api.StorageApi
import io.github.gianpamx.shscale.domain.entity.Settings

class SaveSettings(private val storageApi: StorageApi) {
    operator fun invoke(settings: Settings) {
        storageApi.settings = settings
    }
}
