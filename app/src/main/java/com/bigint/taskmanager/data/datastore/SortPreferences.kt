package com.bigint.taskmanager.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SortPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        val SORT_TYPE_KEY = intPreferencesKey("sort_type")
    }

    // Save data to DataStore
    suspend fun updateCurrentSortOption(sortOptionValue: Int) {
        dataStore.edit { preferences ->
            preferences[SORT_TYPE_KEY] = sortOptionValue
        }
    }

    // Retrieve data from DataStore
    val currentSortOption: Flow<Int> = dataStore.data.map { preferences ->
        preferences[SORT_TYPE_KEY] ?: 1
    }


    suspend fun clearSort() {
        dataStore.edit { preferences ->
            preferences.remove(SORT_TYPE_KEY)
        }
    }
}
