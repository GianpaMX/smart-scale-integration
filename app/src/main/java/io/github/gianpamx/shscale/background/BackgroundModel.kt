package io.github.gianpamx.shscale.background

import io.github.gianpamx.shscale.domain.ObserveSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

class BackgroundModel(
    observeSettings: ObserveSettings,
    private val errorChannel: BroadcastChannel<Throwable>,
    defaultDispatcher: CoroutineDispatcher
) {
    val settings = observeSettings()
        .flowOn(defaultDispatcher)
        .catch { errorChannel.send(it) }
}
