package io.github.gianpamx.shscale.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.gianpamx.shscale.R

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = viewModel()) {
    val settingsState: SettingsState by settingsViewModel.state.observeAsState(SettingsState())
    SettingsContent(
        state = settingsState,
        settingsViewModel::onRunInTheBackgroundChange
    )
}

@Composable
internal fun SettingsContent(
    state: SettingsState,
    onRunInTheBackgroundChange: ((Boolean) -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val runInTheBackgroundLabel = stringResource(R.string.settings_background_run_title)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = runInTheBackgroundLabel,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = stringResource(R.string.settings_background_run_subtitle),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.body2
                )
            }
            Switch(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 8.dp)
                    .semantics { contentDescription = runInTheBackgroundLabel },
                checked = state.runInTheBackground,
                onCheckedChange = onRunInTheBackgroundChange
            )
        }
    }
}
