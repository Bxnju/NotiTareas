package com.benchopo.notitareas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.benchopo.notitareas.ui.screens.login.LoginScreen
import com.benchopo.notitareas.ui.screens.materias.MateriasScreen
import com.benchopo.notitareas.ui.screens.tareas.TareasPorMateriaScreen
import com.benchopo.notitareas.ui.screens.tareas.TareasScreen
import com.benchopo.notitareas.viewModel.AuthViewModel
import com.benchopo.notitareas.viewModel.MateriasViewModel
import com.benchopo.notitareas.viewModel.TareasViewModel

object Routes {
    const val Login = "login"
    const val Materias = "materias"
    const val Tareas = "tareas"
}

@Composable
fun NotiTareasNavGraph(
    navController: NavHostController,
    materiasViewModel: MateriasViewModel,
    tareasViewModel: TareasViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Login, // ← ahora inicia aquí
        modifier = modifier
    ) {
        composable(Routes.Login) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(Routes.Materias) {
            MateriasScreen(
                navController = navController,
                materiasViewModel = materiasViewModel,
                authViewModel = authViewModel,
                onNavigateToTareas = {
                    navController.navigate(Routes.Tareas)
                }
            )
        }

        composable(Routes.Tareas) {
            TareasScreen(
                navController = navController,
                tareasViewModel = tareasViewModel,
                materiasViewModel = materiasViewModel,
                authViewModel = authViewModel
            )
        }

        composable("tareasPorMateria/{materiaNombre}") { backStackEntry ->
            val materiaNombre = backStackEntry.arguments?.getString("materiaNombre") ?: ""
            TareasPorMateriaScreen(
                navController = navController,
                tareasViewModel = tareasViewModel,
                nombreMateria = materiaNombre
            )
        }
    }
}

