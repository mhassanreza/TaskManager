package com.bigint.taskmanager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigint.taskmanager.domain.usecase.preferences.PreferencesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesUseCases: PreferencesUseCases
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow<Boolean>(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    init {
        checkForSelectedLanguage()
    }

    private fun checkForSelectedLanguage() = viewModelScope.launch {
        preferencesUseCases.getCurrentThemeUseCase.invoke().collect {
            _isDarkTheme.value = it
        }
    }

}