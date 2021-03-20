package io.github.gianpamx.shscale.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableLiveData(SettingsState())
    val state: LiveData<SettingsState> = _state

    fun onRunInTheBackgroundChange(newValue: Boolean) {
        _state.value = _state.value?.copy(runInTheBackground = newValue)
    }
}
