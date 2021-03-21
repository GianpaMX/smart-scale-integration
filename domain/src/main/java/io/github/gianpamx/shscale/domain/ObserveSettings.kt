package io.github.gianpamx.shscale.domain

import io.github.gianpamx.shscale.domain.api.StorageApi
import io.github.gianpamx.shscale.domain.entity.Settings
import kotlinx.coroutines.flow.Flow

class ObserveSettings(private val storageApi: StorageApi) {
    operator fun invoke(): Flow<Settings> = storageApi.liveSettings()
}
