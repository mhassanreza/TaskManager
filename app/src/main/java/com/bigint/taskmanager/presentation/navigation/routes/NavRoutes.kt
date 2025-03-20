package com.bigint.taskmanager.presentation.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class NavBaseScreenRoutes {
    @Serializable
    data object HomeScreen : NavBaseScreenRoutes()

    @Serializable
    data object TaskCreationScreen : NavBaseScreenRoutes()

    @Serializable
    data class TaskDetailsScreen(val taskId: Int) : NavBaseScreenRoutes()

    @Serializable
    data object SettingsScreen : NavBaseScreenRoutes()
}
