package io.github.gianpamx.shscale

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.composethemeadapter.MdcTheme
import io.github.gianpamx.shscale.app.ComponentApp
import io.github.gianpamx.shscale.settings.SettingsScreen
import io.github.gianpamx.shscale.settings.SettingsViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ComponentApp).component.inject(this)

        setContent {
            MdcTheme {
                SettingsScreen(viewModel)
            }
        }
    }
}
