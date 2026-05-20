package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.repository.AppRepository
import com.example.data.repository.PreferenceRepository
import com.example.ui.navigation.BottomNavigationBar
import com.example.ui.screens.AiScreen
import com.example.ui.screens.CommandsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SetupWizardScreen
import com.example.ui.screens.SnippetsScreen
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.VeloxkitTheme
import com.example.viewmodel.AiViewModel
import com.example.viewmodel.CommandsViewModel
import com.example.viewmodel.SnippetsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Extract repositories from application container
        val app = application as VeloxkitApplication
        val appRepository = app.appRepository
        val preferenceRepository = app.preferenceRepository

        // Create custom Viewmodel Factory
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SnippetsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SnippetsViewModel(appRepository) as T
                }
                if (modelClass.isAssignableFrom(AiViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return AiViewModel(appRepository, preferenceRepository, applicationContext) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        setContent {
            VeloxkitTheme {
                val navController = rememberNavController()

                // Instantiate ViewModels with Factories
                val snippetsViewModel: SnippetsViewModel = viewModel(factory = factory)
                val aiViewModel: AiViewModel = viewModel(factory = factory)
                val commandsViewModel: CommandsViewModel = viewModel()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CyberBackground),
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    },
                    containerColor = CyberBackground,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0) // Handle edge-to-edge manually
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(CyberBackground)
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable("home") {
                                HomeScreen(navController = navController)
                            }
                            composable("commands") {
                                CommandsScreen(viewModel = commandsViewModel)
                            }
                            composable("snippets") {
                                SnippetsScreen(viewModel = snippetsViewModel)
                            }
                            composable("projects") {
                                ProjectsScreen()
                            }
                            composable("ai") {
                                AiScreen(viewModel = aiViewModel)
                            }
                            composable("settings") {
                                SettingsScreen(
                                    aiViewModel = aiViewModel,
                                    snippetsViewModel = snippetsViewModel
                                )
                            }
                            composable("setup_wizard") {
                                SetupWizardScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
