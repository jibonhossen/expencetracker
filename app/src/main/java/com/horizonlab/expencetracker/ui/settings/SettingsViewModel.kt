package com.horizonlab.expencetracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizonlab.expencetracker.data.dao.AppDao
import com.horizonlab.expencetracker.data.entity.SettingsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val id: Int = 0,
    val name: String = "User",
    val monthlyBudget: String = "0",
    val currencySymbol: String = "$",
    val isLoaded: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dao: AppDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            dao.getSettings().collect { settings ->
                if (settings != null) {
                    _uiState.value = SettingsUiState(
                        id = settings.id,
                        name = settings.name,
                        monthlyBudget = settings.monthlyBudget.toString(),
                        currencySymbol = settings.currencySymbol,
                        isLoaded = true
                    )
                } else {
                    // Insert default settings on first run
                    val default = SettingsEntity()
                    dao.insertSettings(default)
                }
            }
        }
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
        saveSettings()
    }

    fun updateBudget(budget: String) {
        _uiState.value = _uiState.value.copy(monthlyBudget = budget)
        saveSettings()
    }

    fun updateCurrencySymbol(symbol: String) {
        _uiState.value = _uiState.value.copy(currencySymbol = symbol)
        saveSettings()
    }

    private fun saveSettings() {
        viewModelScope.launch {
            val state = _uiState.value
            val budgetInt = state.monthlyBudget.toIntOrNull() ?: 0
            dao.updateSettings(
                SettingsEntity(
                    id = state.id,
                    name = state.name,
                    monthlyBudget = budgetInt,
                    currencySymbol = state.currencySymbol
                )
            )
        }
    }
}
