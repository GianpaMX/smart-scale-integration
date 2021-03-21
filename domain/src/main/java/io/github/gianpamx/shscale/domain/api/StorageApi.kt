package io.github.gianpamx.shscale.domain.api

import io.github.gianpamx.shscale.domain.entity.Settings
import kotlinx.coroutines.flow.Flow

interface StorageApi {
    var settings: Settings
    fun liveSettings(): Flow<Settings>
}
