package com.benchopo.notitareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.benchopo.notitareas.ui.navigation.NotiTareasNavGraph
import com.benchopo.notitareas.ui.theme.NotiTareasTheme
import com.benchopo.notitareas.viewModel.MateriasViewModel
import com.benchopo.notitareas.viewModel.TareasViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.benchopo.notitareas.viewModel.AuthViewModel
import com.benchopo.notitareas.viewModel.UsuariosViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {
            NotiTareasTheme {
                Surface {
                    val navController = rememberNavController()
                    val materiasViewModel: MateriasViewModel = viewModel()
                    val tareasViewModel: TareasViewModel = viewModel()
                    val authViewModel: AuthViewModel = viewModel()
                    val usuariosViewModel: UsuariosViewModel = viewModel()
                    NotiTareasNavGraph(
                        navController = navController,
                        materiasViewModel = materiasViewModel,
                        tareasViewModel = tareasViewModel,
                        authViewModel = authViewModel,
                        usuariosViewModel = usuariosViewModel
                    )
                }
            }
        }
    }
}
