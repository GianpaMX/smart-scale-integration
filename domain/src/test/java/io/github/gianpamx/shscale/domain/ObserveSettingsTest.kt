package io.github.gianpamx.shscale.domain

import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.github.gianpamx.shscale.domain.api.StorageApi
import io.github.gianpamx.shscale.domain.entity.Settings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class ObserveSettingsTest {
    private val storageApi: StorageApi = mock()

    @Test
    fun `Observe Settings`() = runBlockingTest {
        whenever(storageApi.liveSettings()).thenReturn(flowOf(Settings(runInTheBackground = true)))
        val observeSettings = ObserveSettings(storageApi)

        observeSettings().test {
            assertThat(expectItem().runInTheBackground).isEqualTo(true)
            expectComplete()
        }
    }
}
