package com.bigint.taskmanager.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FilterPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        val FILTER_OPTION_KEY = intPreferencesKey("filter_options")
    }

    // Save data to DataStore
    suspend fun updateCurrentFilterOption(filterOptionValue: Int) {
        dataStore.edit { preferences ->
            preferences[FILTER_OPTION_KEY] = filterOptionValue
        }
    }

    // Retrieve data from DataStore
    val currentFilterOption: Flow<Int> = dataStore.data.map { preferences ->
        preferences[FILTER_OPTION_KEY] ?: 1
    }


    suspend fun clearFilterOption() {
        dataStore.edit { preferences ->
            preferences.remove(FILTER_OPTION_KEY)
        }
    }
}
