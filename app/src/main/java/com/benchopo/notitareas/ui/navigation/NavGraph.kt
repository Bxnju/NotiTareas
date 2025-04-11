package com.benchopo.notitareas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.benchopo.notitareas.ui.screens.materias.MateriasScreen
import com.benchopo.notitareas.ui.screens.tareas.TareasScreen
import com.benchopo.notitareas.viewModel.MateriasViewModel
import com.benchopo.notitareas.viewModel.TareasViewModel

object Routes {
    const val Materias = "materias"
    const val Tareas = "tareas"
}

@Composable
fun NotiTareasNavGraph(
    navController: NavHostController,
    materiasViewModel: MateriasViewModel,
    tareasViewModel: TareasViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Materias,
        modifier = modifier
    ) {
        composable(Routes.Materias) {
            MateriasScreen( materiasViewModel = materiasViewModel,
                onNavigateToTareas = {
                navController.navigate(Routes.Tareas)
            })
        }
        composable(Routes.Tareas) {
            TareasScreen( navController = navController, materiasViewModel = materiasViewModel,
                tareasViewModel = tareasViewModel)
        }
    }
}
