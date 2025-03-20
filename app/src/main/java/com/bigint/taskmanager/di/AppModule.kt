package com.bigint.taskmanager.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.bigint.taskmanager.data.datastore.FilterPreferences
import com.bigint.taskmanager.data.datastore.SortPreferences
import com.bigint.taskmanager.data.datastore.ThemePreferences
import com.bigint.taskmanager.data.db.TaskDao
import com.bigint.taskmanager.data.db.TaskDatabase
import com.bigint.taskmanager.data.repository.PreferencesRepositoryImpl
import com.bigint.taskmanager.data.repository.TaskRepositoryImpl
import com.bigint.taskmanager.domain.repository.IPreferencesRepository
import com.bigint.taskmanager.domain.repository.ITaskRepository
import com.bigint.taskmanager.domain.usecase.AddTaskUseCase
import com.bigint.taskmanager.domain.usecase.DeleteTaskUseCase
import com.bigint.taskmanager.domain.usecase.GetTasksUseCase
import com.bigint.taskmanager.domain.usecase.UpdateTaskUseCase
import com.bigint.taskmanager.domain.usecase.preferences.GetCurrentFilterOptionUseCase
import com.bigint.taskmanager.domain.usecase.preferences.GetCurrentSortOptionUseCase
import com.bigint.taskmanager.domain.usecase.preferences.GetCurrentThemeUseCase
import com.bigint.taskmanager.domain.usecase.preferences.PreferencesUseCases
import com.bigint.taskmanager.domain.usecase.preferences.UpdateCurrentSortOptionUseCase
import com.bigint.taskmanager.domain.usecase.preferences.UpdateCurrentThemeUseCase
import com.bigint.taskmanager.domain.usecase.preferences.UpdateFilterOptionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase =
        Room.databaseBuilder(context, TaskDatabase::class.java, "task_manager_db").build()

    @Provides
    @Singleton
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao): ITaskRepository = TaskRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideAddTaskUseCase(repository: ITaskRepository) = AddTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTasksUseCase(repository: ITaskRepository) = GetTasksUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateTaskUseCase(repository: ITaskRepository) = UpdateTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteTaskUseCase(repository: ITaskRepository) = DeleteTaskUseCase(repository)


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.dataStoreFile("task_manager_prefs.preferences_pb") }
        )

    @Provides
    @Singleton
    fun providePreferencesRepository(
        themePreferences: ThemePreferences,
        filterPreferences: FilterPreferences,
        sortPreferences: SortPreferences
    ): IPreferencesRepository =
        PreferencesRepositoryImpl(themePreferences, filterPreferences, sortPreferences)

    @Provides
    @Singleton
    fun providePreferencesUseCases(repository: IPreferencesRepository): PreferencesUseCases =
        PreferencesUseCases(
            updateCurrentThemeUseCase = UpdateCurrentThemeUseCase(repository),
            getCurrentThemeUseCase = GetCurrentThemeUseCase(repository),
            updateFilterOptionUseCase = UpdateFilterOptionUseCase(repository),
            getCurrentFilterOptionUseCase = GetCurrentFilterOptionUseCase(repository),
            updateCurrentSortOptionUseCase = UpdateCurrentSortOptionUseCase(repository),
            getCurrentSortOptionUseCase = GetCurrentSortOptionUseCase(repository)
        )
}