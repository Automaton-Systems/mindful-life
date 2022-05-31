package com.systems.automaton.mindfullife.presentation.main.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.systems.automaton.mindfullife.presentation.main.DashboardScreen
import com.systems.automaton.mindfullife.presentation.main.SettingsScreen
import com.systems.automaton.mindfullife.presentation.main.SpacesScreen
import com.systems.automaton.mindfullife.presentation.util.Screen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    mainNavController: NavHostController,
    startUpScreen: String
) {
    NavHost(navController = navController, startDestination = startUpScreen){

        composable(Screen.DashboardScreen.route){
            DashboardScreen(mainNavController)
        }
        composable(Screen.SpacesScreen.route){
            SpacesScreen(mainNavController)
        }
        composable(Screen.SettingsScreen.route){
            SettingsScreen()
        }
    }
}