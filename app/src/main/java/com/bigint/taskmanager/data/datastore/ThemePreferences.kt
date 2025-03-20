package com.bigint.taskmanager.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemePreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        val CURRENT_THEME_KEY = booleanPreferencesKey("is_dark_theme")
    }

    // Save data to DataStore
    suspend fun updateCurrentTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[CURRENT_THEME_KEY] = isDarkTheme
        }
    }

    // Retrieve data from DataStore
    val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[CURRENT_THEME_KEY] == true
    }


    suspend fun clearTheme() {
        dataStore.edit { preferences ->
            preferences.remove(CURRENT_THEME_KEY)
        }
    }
}
