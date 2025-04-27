package com.benchopo.notitareas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.benchopo.notitareas.ui.screens.login.LoginScreen
import com.benchopo.notitareas.ui.screens.materias.MateriasScreen
import com.benchopo.notitareas.ui.screens.register.RegisterScreen
import com.benchopo.notitareas.ui.screens.tareas.TareasPorMateriaScreen
import com.benchopo.notitareas.ui.screens.tareas.TareasScreen
import com.benchopo.notitareas.viewModel.AuthViewModel
import com.benchopo.notitareas.viewModel.MateriasViewModel
import com.benchopo.notitareas.viewModel.TareasViewModel
import com.benchopo.notitareas.viewModel.UsuariosViewModel

object Routes {
    const val Login = "login"
    const val Materias = "materias"
    const val Tareas = "tareas"
    const val Register = "register"
}

@Composable
fun NotiTareasNavGraph(
    navController: NavHostController,
    materiasViewModel: MateriasViewModel,
    tareasViewModel: TareasViewModel,
    authViewModel: AuthViewModel,
    usuariosViewModel: UsuariosViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Login,
        modifier = modifier
    ) {
        composable(Routes.Login) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        composable("register") {
            RegisterScreen(navController = navController, authViewModel = authViewModel, usuariosViewModel = usuariosViewModel)
        }


        composable(Routes.Materias) {
            MateriasScreen(
                navController = navController,
                materiasViewModel = materiasViewModel,
                authViewModel = authViewModel,
                usuariosViewModel = usuariosViewModel,
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
                authViewModel = authViewModel,
                usuariosViewModel = usuariosViewModel
            )
        }

        composable("tareasPorMateria/{materiaNombre}") { backStackEntry ->
            val materiaNombre = backStackEntry.arguments?.getString("materiaNombre") ?: ""
            TareasPorMateriaScreen(
                navController = navController,
                authViewModel = authViewModel,
                tareasViewModel = tareasViewModel,
                nombreMateria = materiaNombre
            )
        }
    }
}

