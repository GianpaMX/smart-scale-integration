package io.github.gianpamx.shscale.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.github.gianpamx.MainCoroutineRule
import io.github.gianpamx.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        viewModel = SettingsViewModel()
    }

    @Test
    fun `Initial State`() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.state.observeForTesting {
            assertThat(viewModel.state.value?.runInTheBackground).isEqualTo(false)
        }
    }

    @Test
    fun `Turn on`() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.onRunInTheBackgroundChange(true)

        viewModel.state.observeForTesting {
            assertThat(viewModel.state.value?.runInTheBackground).isEqualTo(true)
        }
    }
}
