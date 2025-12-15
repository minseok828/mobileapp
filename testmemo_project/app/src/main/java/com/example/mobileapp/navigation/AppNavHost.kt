package com.example.mobileapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.mobileapp.data.TaskViewModel
import com.example.mobileapp.ui.screens.TaskDetailScreen
import com.example.mobileapp.ui.screens.TaskEditScreen
import com.example.mobileapp.ui.screens.TaskListScreen

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    val vm: TaskViewModel = viewModel()

    NavHost(
        navController = nav,
        startDestination = Routes.LIST
    ) {
        composable(Routes.LIST) {
            TaskListScreen(
                vm = vm,
                onOpenDetail = { id -> nav.navigate(Routes.detail(id)) },
                onOpenAdd = { nav.navigate(Routes.edit(null)) }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments?.getLong("id") ?: return@composable
            val task = vm.getById(id) ?: return@composable

            TaskDetailScreen(
                task = task,
                onBack = { nav.popBackStack() },
                onEdit = { nav.navigate(Routes.edit(id)) },
                onDelete = {
                    vm.delete(id)
                    nav.popBackStack()
                },
                onToggleDone = { vm.toggleDone(id) }
            )
        }

        composable(Routes.EDIT) { backStack ->
            val taskId = backStack.arguments?.getString("taskId")?.toLongOrNull() ?: -1L
            val initial = if (taskId == -1L) null else vm.getById(taskId)

            TaskEditScreen(
                initial = initial,
                onCancel = { nav.popBackStack() },
                onSave = { saved ->
                    if (initial == null) vm.add(saved) else vm.update(saved)
                    nav.popBackStack()
                }
            )
        }
    }
}
