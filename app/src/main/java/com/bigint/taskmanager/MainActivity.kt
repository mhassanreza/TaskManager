package com.bigint.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.bigint.taskmanager.presentation.MainViewModel
import com.bigint.taskmanager.presentation.navigation.RootNavGraph
import com.bigint.taskmanager.presentation.navigation.routes.NavBaseScreenRoutes
import com.bigint.taskmanager.presentation.ui.theme.TaskManagerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainApp()
        }
    }

    @Composable
    fun MainApp(viewModel: MainViewModel = hiltViewModel()) {
        val isDarkTheme by viewModel.isDarkTheme.collectAsState()
        TaskManagerAppTheme(darkTheme = isDarkTheme) {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                RootNavGraph(
                    NavBaseScreenRoutes.HomeScreen
                )
            }
        }
    }
}


