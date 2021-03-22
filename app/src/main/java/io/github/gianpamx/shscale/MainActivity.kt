package io.github.gianpamx.shscale

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.composethemeadapter.MdcTheme
import io.github.gianpamx.shscale.app.ComponentApp
import io.github.gianpamx.shscale.background.BG_SERVICE_EVENT
import io.github.gianpamx.shscale.background.BackgroundEvent
import io.github.gianpamx.shscale.background.BackgroundEvent.ACTIVITY_STARTED
import io.github.gianpamx.shscale.background.BackgroundEvent.ACTIVITY_STOPPED
import io.github.gianpamx.shscale.background.BackgroundService
import io.github.gianpamx.shscale.settings.SettingsScreen
import io.github.gianpamx.shscale.settings.SettingsViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted ->
            if (!isGranted) viewModel.onRunInTheBackgroundChange(false)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ComponentApp).component.inject(this)

        viewModel.state.observe(this) {
            if (it.isBackgroundServiceEnabled && isPermissionGranted().not()) {
                requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
            }
        }

        setContent {
            MdcTheme {
                SettingsScreen(viewModel)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        sendBackgroundEvent(ACTIVITY_STARTED)
    }

    override fun onStop() {
        super.onStop()
        sendBackgroundEvent(ACTIVITY_STOPPED)
    }


    private fun sendBackgroundEvent(event: BackgroundEvent) =
        startService(buildCommandIntent((event)))

    private fun buildCommandIntent(event: BackgroundEvent) =
        Intent(this, BackgroundService::class.java).apply {
            putExtra(BG_SERVICE_EVENT, event.name)
        }
}
