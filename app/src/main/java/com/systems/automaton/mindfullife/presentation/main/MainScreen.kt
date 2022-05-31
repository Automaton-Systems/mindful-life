package com.systems.automaton.mindfullife.presentation.main

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.systems.automaton.mindfullife.presentation.main.components.MainBottomBar
import com.systems.automaton.mindfullife.presentation.main.components.NavigationGraph
import com.systems.automaton.mindfullife.presentation.util.BottomNavItem


@Composable
fun MainScreen(
    startUpScreen: String,
    mainNavController: NavHostController
) {
    val navController = rememberNavController()
    val bottomNavItems =
        listOf(BottomNavItem.Dashboard, BottomNavItem.Spaces, BottomNavItem.Settings)
    Scaffold(
        bottomBar = {
            MainBottomBar(navController = navController, items = bottomNavItems)
        }
    ) {
        NavigationGraph(
            navController = navController,
            mainNavController = mainNavController,
            startUpScreen = startUpScreen
        )
    }
}