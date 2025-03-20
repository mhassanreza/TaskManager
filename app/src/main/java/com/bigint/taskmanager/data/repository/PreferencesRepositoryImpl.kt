package com.bigint.taskmanager.data.repository

import com.bigint.taskmanager.data.datastore.FilterPreferences
import com.bigint.taskmanager.data.datastore.SortPreferences
import com.bigint.taskmanager.data.datastore.ThemePreferences
import com.bigint.taskmanager.domain.repository.IPreferencesRepository
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(
    private val themePreferences: ThemePreferences,
    private val filterPreferences: FilterPreferences,
    private val sortPreferences: SortPreferences
) : IPreferencesRepository {
    override suspend fun updateCurrentTheme(isDarkTheme: Boolean) {
        themePreferences.updateCurrentTheme(isDarkTheme)
    }

    override fun getCurrentTheme(): Flow<Boolean> {
        return themePreferences.isDarkTheme
    }

    override suspend fun updateFilterOption(filterOptionValue: Int) {
        filterPreferences.updateCurrentFilterOption(filterOptionValue)
    }

    override fun getCurrentFilterOption(): Flow<Int> {
        return filterPreferences.currentFilterOption
    }

    override suspend fun updateCurrentSortOption(sortOptionValue: Int) {
        sortPreferences.updateCurrentSortOption(sortOptionValue)
    }

    override fun getCurrentSortOption(): Flow<Int> {
        return sortPreferences.currentSortOption
    }
}