package io.github.gianpamx.shscale.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import io.github.gianpamx.MainCoroutineRule
import io.github.gianpamx.observeForTesting
import io.github.gianpamx.shscale.domain.ObserveSettings
import io.github.gianpamx.shscale.domain.SaveSettings
import io.github.gianpamx.shscale.domain.entity.Settings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {
    private val settingsFlow = MutableStateFlow(Settings(runInTheBackground = false))
    private val observeSettings: ObserveSettings = mock()
    private val saveSettings: SaveSettings = mock()
    private val errorChannel: BroadcastChannel<Throwable> = BroadcastChannel(Channel.CONFLATED)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        whenever(observeSettings.invoke()).doReturn(settingsFlow)

        viewModel = SettingsViewModel(
            observeSettings,
            saveSettings,
            errorChannel,
            coroutineRule.testDispatcher
        )
    }

    @Test
    fun `Initial State`() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.state.observeForTesting {
            assertThat(viewModel.state.value?.runInTheBackground).isEqualTo(false)
        }
    }

    @Test
    fun `Turn on`() = coroutineRule.testDispatcher.runBlockingTest {
        doAnswer {
            settingsFlow.value = it.arguments[0] as Settings
        }.whenever(saveSettings).invoke(any())

        viewModel.onRunInTheBackgroundChange(true)

        viewModel.state.observeForTesting {
            assertThat(viewModel.state.value?.runInTheBackground).isEqualTo(true)
        }
    }
}
