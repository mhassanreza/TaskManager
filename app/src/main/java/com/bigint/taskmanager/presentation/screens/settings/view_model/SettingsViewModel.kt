package com.bigint.taskmanager.presentation.screens.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigint.taskmanager.domain.enums.FilterOption
import com.bigint.taskmanager.domain.enums.SortOption
import com.bigint.taskmanager.domain.usecase.preferences.PreferencesUseCases
import com.bigint.taskmanager.presentation.screens.settings.intent.SettingsIntent
import com.bigint.taskmanager.presentation.screens.settings.state.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesUseCases: PreferencesUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        checkSavedValuesInPreferences()
    }

    fun processIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.UpdateSort -> updateSortOptionPreferences(intent.sortBy)
            is SettingsIntent.UpdateFilter -> updateFilterOptionPreferences(intent.filterBy)
            is SettingsIntent.UpdateTheme -> updateThemePreferences(intent.isDark)
        }
    }

    private fun checkSavedValuesInPreferences() {
        viewModelScope.launch {
            preferencesUseCases.getCurrentThemeUseCase.invoke().collect {
                _state.value = _state.value.copy(
                    isDarkTheme = it
                )
            }
        }
        viewModelScope.launch {
            preferencesUseCases.getCurrentSortOptionUseCase.invoke().collect {
                _state.value = _state.value.copy(
                    sortBy = SortOption.fromValue(it)
                )
            }
        }
        viewModelScope.launch {
            preferencesUseCases.getCurrentFilterOptionUseCase.invoke().collect {
                _state.value = _state.value.copy(
                    filterBy = FilterOption.fromValue(it)
                )
            }
        }
    }

    private fun updateThemePreferences(isDarkTheme: Boolean) = viewModelScope.launch {
        preferencesUseCases.updateCurrentThemeUseCase(isDarkTheme = isDarkTheme)
    }

    private fun updateSortOptionPreferences(sortBy: SortOption) = viewModelScope.launch {
        preferencesUseCases.updateCurrentSortOptionUseCase(sortOptionValue = sortBy.value)
    }

    private fun updateFilterOptionPreferences(filterOption: FilterOption) = viewModelScope.launch {
        preferencesUseCases.updateFilterOptionUseCase(filterOptionValue = filterOption.value)
    }
}