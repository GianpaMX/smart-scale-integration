package io.github.gianpamx.shscale.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import io.github.gianpamx.shscale.R
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SettingsScreenKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    var state by mutableStateOf(SettingsState())

    lateinit var targetContext: Context

    @Before
    fun setUp() {
        targetContext = InstrumentationRegistry
            .getInstrumentation()
            .targetContext


        composeTestRule.setContent {
            SettingsContent(state) {
                state = state.copy(runInTheBackground = it)
            }
        }
    }

    @Test
    fun initialStateOff() {
        val runInTheBackgroundSwitch = composeTestRule
            .onNodeWithContentDescription(getString(R.string.settings_background_run_title))

        runInTheBackgroundSwitch.assert(isOff())
    }

    @Test
    fun initialStateOn() {
        state = state.copy(runInTheBackground = true)

        val runInTheBackgroundSwitch = composeTestRule
            .onNodeWithContentDescription(getString(R.string.settings_background_run_title))

        runInTheBackgroundSwitch.assert(isOn())
    }

    @Test
    fun turnOnRunInTheBackground() {
        val bgSwitch = composeTestRule
            .onNodeWithContentDescription(getString(R.string.settings_background_run_title))

        bgSwitch.performClick()

        assertTrue(state.runInTheBackground)
    }

    private fun getString(resId: Int) = targetContext.getString(resId)
}
