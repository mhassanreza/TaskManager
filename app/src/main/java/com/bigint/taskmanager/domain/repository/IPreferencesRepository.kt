package com.bigint.taskmanager.domain.repository

import kotlinx.coroutines.flow.Flow

interface IPreferencesRepository {
    suspend fun updateCurrentTheme(isDarkTheme: Boolean)
    fun getCurrentTheme(): Flow<Boolean>

    suspend fun updateFilterOption(filterOptionValue: Int)
    fun getCurrentFilterOption(): Flow<Int>

    suspend fun updateCurrentSortOption(sortOptionValue: Int)
    fun getCurrentSortOption(): Flow<Int>
}
