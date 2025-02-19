package com.systems.automaton.mindfullife.presentation.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.systems.automaton.mindfullife.R
import com.systems.automaton.mindfullife.presentation.util.Screen
import com.systems.automaton.mindfullife.util.Constants

@Composable
fun TasksSearchScreen(
    navController: NavHostController,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val state = viewModel.tasksUiState
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        var query by rememberSaveable {
            mutableStateOf("")
        }
        LaunchedEffect(query){viewModel.onEvent(TaskEvent.SearchTasks(query))}
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(true){focusRequester.requestFocus()}
        OutlinedTextField(
            value = query,
            onValueChange = {query = it},
            label = {Text(stringResource(R.string.search_tasks))},
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
        ) {
            items(state.searchTasks, key = {it.id}){ task ->
                TaskItem(
                    task = task,
                    onComplete = {
                        viewModel.onEvent(
                            TaskEvent.CompleteTask(
                                task,
                                !task.isCompleted
                            )
                        )
                    },
                    onClick = {
                        navController.navigate(
                            Screen.TaskDetailScreen.route.replace(
                                "{${Constants.TASK_ID_ARG}}",
                                "${task.id}"
                            )
                        )
                    },
                )
            }
        }
    }
}