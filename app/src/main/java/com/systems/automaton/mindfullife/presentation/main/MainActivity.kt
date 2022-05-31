package com.systems.automaton.mindfullife.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.systems.automaton.mindfullife.presentation.bookmarks.BookmarkDetailsScreen
import com.systems.automaton.mindfullife.presentation.bookmarks.BookmarkSearchScreen
import com.systems.automaton.mindfullife.presentation.bookmarks.BookmarksScreen
import com.systems.automaton.mindfullife.presentation.calendar.CalendarScreen
import com.systems.automaton.mindfullife.presentation.diary.DiaryChartScreen
import com.systems.automaton.mindfullife.presentation.diary.DiaryEntryDetailsScreen
import com.systems.automaton.mindfullife.presentation.diary.DiaryScreen
import com.systems.automaton.mindfullife.presentation.diary.DiarySearchScreen
import com.systems.automaton.mindfullife.presentation.notes.NoteDetailsScreen
import com.systems.automaton.mindfullife.presentation.notes.NotesScreen
import com.systems.automaton.mindfullife.presentation.notes.NotesSearchScreen
import com.systems.automaton.mindfullife.presentation.tasks.TaskDetailScreen
import com.systems.automaton.mindfullife.presentation.tasks.TasksScreen
import com.systems.automaton.mindfullife.presentation.tasks.TasksSearchScreen
import com.systems.automaton.mindfullife.presentation.util.Screen
import com.systems.automaton.mindfullife.theme.DarkBackground
import com.systems.automaton.mindfullife.theme.MyBrainTheme
import com.systems.automaton.mindfullife.util.Constants
import com.systems.automaton.mindfullife.util.settings.StartUpScreenSettings
import com.systems.automaton.mindfullife.util.settings.ThemeSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Suppress("BlockingMethodInNonBlockingContext")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val themMode = viewModel.themMode.collectAsState(initial = ThemeSettings.AUTO.value)
            var startUpScreenSettings by remember { mutableStateOf(StartUpScreenSettings.SPACES.value) }
            val systemUiController = rememberSystemUiController()
            LaunchedEffect(true) {
                runBlocking {
                    startUpScreenSettings = viewModel.defaultStartUpScreen.first()
                }
            }
            val startUpScreen =
                if (startUpScreenSettings == StartUpScreenSettings.SPACES.value)
                    Screen.SpacesScreen.route else Screen.DashboardScreen.route
            val isDarkMode = when (themMode.value) {
                ThemeSettings.DARK.value -> true
                ThemeSettings.LIGHT.value -> false
                else -> isSystemInDarkTheme()
            }
            val color = MaterialTheme.colors.background
            println("red: ${color.toArgb()}, green: ${color.green}, blue: ${color.blue}")
            SideEffect {
                systemUiController.setSystemBarsColor(
                    if (isDarkMode) DarkBackground else Color.White,
                    darkIcons = !isDarkMode
                )
            }
            MyBrainTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(
                        startDestination = Screen.Main.route,
                        navController = navController
                    ) {
                        composable(Screen.Main.route) {
                            MainScreen(
                                startUpScreen = startUpScreen,
                                mainNavController = navController
                            )
                        }
                        composable(
                            Screen.TasksScreen.route,
                            arguments = listOf(navArgument(Constants.ADD_TASK_ARG) {
                                type = NavType.BoolType
                                defaultValue = false
                            }),
                            deepLinks =
                            listOf(
                                navDeepLink {
                                    uriPattern =
                                        "${Constants.TASKS_SCREEN_URI}/{${Constants.ADD_TASK_ARG}}"
                                }
                            )
                        ) {
                            TasksScreen(
                                navController = navController,
                                addTask = it.arguments?.getBoolean(Constants.ADD_TASK_ARG)
                                    ?: false
                            )
                        }
                        composable(
                            Screen.TaskDetailScreen.route,
                            arguments = listOf(navArgument(Constants.TASK_ID_ARG) {
                                type = NavType.IntType
                            }),
                            deepLinks =
                            listOf(
                                navDeepLink {
                                    uriPattern =
                                        "${Constants.TASK_DETAILS_URI}/{${Constants.TASK_ID_ARG}}"
                                }
                            )
                        ) {
                            TaskDetailScreen(
                                navController = navController,
                                it.arguments?.getInt(Constants.TASK_ID_ARG)!!
                            )
                        }
                        composable(Screen.TaskSearchScreen.route) {
                            TasksSearchScreen(navController = navController)
                        }
                        composable(
                            Screen.NotesScreen.route
                        ) {
                            NotesScreen(navController = navController)
                        }
                        composable(Screen.NoteAddScreen.route) {}
                        composable(
                            Screen.NoteDetailsScreen.route,
                            arguments = listOf(navArgument(Constants.NOTE_ID_ARG) {
                                type = NavType.IntType
                            })
                        ) {
                            NoteDetailsScreen(
                                navController,
                                it.arguments?.getInt(Constants.NOTE_ID_ARG)!!
                            )
                        }
                        composable(Screen.NoteSearchScreen.route) {
                            NotesSearchScreen(navController = navController)
                        }
                        composable(Screen.DiaryScreen.route) {
                            DiaryScreen(navController = navController)
                        }
                        composable(Screen.DiaryChartScreen.route) {
                            DiaryChartScreen()
                        }
                        composable(Screen.DiarySearchScreen.route) {
                            DiarySearchScreen(navController = navController)
                        }
                        composable(
                            Screen.DiaryDetailScreen.route,
                            arguments = listOf(navArgument(Constants.DIARY_ID_ARG) {
                                type = NavType.IntType
                            })
                        ) {
                            DiaryEntryDetailsScreen(
                                navController = navController,
                                it.arguments?.getInt(Constants.DIARY_ID_ARG)!!
                            )
                        }
                        composable(Screen.BookmarksScreen.route) {
                            BookmarksScreen(navController = navController)
                        }
                        composable(
                            Screen.BookmarkDetailScreen.route,
                            arguments = listOf(navArgument(Constants.BOOKMARK_ID_ARG) {
                                type = NavType.IntType
                            })
                        ) {
                            BookmarkDetailsScreen(
                                navController = navController,
                                it.arguments?.getInt(Constants.BOOKMARK_ID_ARG)!!
                            )
                        }
                        composable(Screen.BookmarkSearchScreen.route) {
                            BookmarkSearchScreen(navController = navController)
                        }
                        composable(
                            Screen.CalendarScreen.route,
                            deepLinks = listOf(
                                navDeepLink {
                                    uriPattern = Constants.CALENDAR_SCREEN_URI
                                }
                            )
                        ) {
                            CalendarScreen()
                        }
                        composable(Screen.CalendarSearchScreen.route) {}
                    }
                }
            }
        }
    }
}