package com.bigint.taskmanager.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bigint.taskmanager.presentation.navigation.routes.NavBaseScreenRoutes
import com.bigint.taskmanager.presentation.screens.home.HomeScreen
import com.bigint.taskmanager.presentation.screens.settings.SettingsScreen
import com.bigint.taskmanager.presentation.screens.task_creation.TaskCreationScreen
import com.bigint.taskmanager.presentation.screens.task_details.TaskDetailsScreen

@Composable
fun RootNavGraph(
    startDestination: NavBaseScreenRoutes
) {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = startDestination
    ) {
        composable<NavBaseScreenRoutes.HomeScreen>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }, // Start from right
                    animationSpec = tween(durationMillis = 700)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth }, // Exit to left
                    animationSpec = tween(durationMillis = 700)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth }, // Enter from left on back
                    animationSpec = tween(durationMillis = 700)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }, // Exit to right on back
                    animationSpec = tween(durationMillis = 700)
                )
            }
        ) {
            HomeScreen(
                goToTaskCreationScreen = {
                    rootNavController.navigate(NavBaseScreenRoutes.TaskCreationScreen)
                },
                goToTaskDetailScreen = { taskId ->
                    rootNavController.navigate(NavBaseScreenRoutes.TaskDetailsScreen(taskId))
                },
                goToSettingsScreen = {
                    rootNavController.navigate(NavBaseScreenRoutes.SettingsScreen)
                }
            )
        }

        composable<NavBaseScreenRoutes.TaskCreationScreen>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            }
        ) {
            TaskCreationScreen(goBackToHome = {
                rootNavController.navigateUp()
            })
        }

        composable<NavBaseScreenRoutes.TaskDetailsScreen>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            }
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<NavBaseScreenRoutes.TaskDetailsScreen>()
            TaskDetailsScreen(
                goBackToHome = {
                    rootNavController.navigateUp()
                },
                taskId = route.taskId
            )
        }

        composable<NavBaseScreenRoutes.SettingsScreen>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 700)
                )
            }
        ) {
            SettingsScreen(goBackToHome = {
                rootNavController.navigateUp()
            })
        }
    }
}
//@Composable
//fun RootNavGraph(
//    startDestination: NavBaseScreenRoutes
//) {
//    val rootNavController = rememberNavController()
//    NavHost(
//        navController = rootNavController, startDestination = startDestination
//    ) {
//        composable<NavBaseScreenRoutes.HomeScreen> {
//            HomeScreen(goToTaskCreationScreen = {
//                rootNavController.navigate(NavBaseScreenRoutes.TaskCreationScreen)
//            }, goToTaskDetailScreen = { taskId ->
//                rootNavController.navigate(NavBaseScreenRoutes.TaskDetailsScreen(taskId))
//            }, goToSettingsScreen = {
//                rootNavController.navigate(NavBaseScreenRoutes.SettingsScreen)
//            })
//        }
//        composable<NavBaseScreenRoutes.TaskCreationScreen> {
//            TaskCreationScreen(goBackToHome = {
//                rootNavController.navigateUp()
//            })
//        }
//        composable<NavBaseScreenRoutes.TaskDetailsScreen> { backStackEntry ->
//            val route = backStackEntry.toRoute<NavBaseScreenRoutes.TaskDetailsScreen>()
//            TaskDetailsScreen(goBackToHome = {
//                rootNavController.navigateUp()
//            }, taskId = route.taskId)
//        }
//        composable<NavBaseScreenRoutes.SettingsScreen> {
//            SettingsScreen(goBackToHome = {
//                rootNavController.navigateUp()
//            })
//        }
//    }
//}