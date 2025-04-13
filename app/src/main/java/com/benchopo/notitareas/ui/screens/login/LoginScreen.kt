package com.benchopo.notitareas.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.ui.components.Snackbar
import com.benchopo.notitareas.ui.components.rememberSnackbarHostState
import com.benchopo.notitareas.viewModel.AuthViewModel
import com.benchopo.notitareas.ui.navigation.Routes
import com.benchopo.notitareas.viewModel.UsuariosViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    usuariosViewModel: UsuariosViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf<Rol?>(null) }

    val snackbarHostState = rememberSnackbarHostState()
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Snackbar(snackbarHostState)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("NotiTareas", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Tu nombre") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { rolSeleccionado = Rol.PROFESOR },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rolSeleccionado == Rol.PROFESOR) MaterialTheme.colorScheme.primary else Color.Gray,
                        contentColor = Color.White
                    )
                ) {
                    Text("Profesor")
                }
                Button(
                    onClick = { rolSeleccionado = Rol.ESTUDIANTE },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rolSeleccionado == Rol.ESTUDIANTE) MaterialTheme.colorScheme.primary else Color.Gray,
                        contentColor = Color.White
                    )
                ) {
                    Text("Estudiante")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.isNotBlank() && rolSeleccionado != null) {
                        val error =
                            authViewModel.login(nombre, rolSeleccionado!!, usuariosViewModel)
                        if (error != null) {
                            snackbarMessage = error
                        } else {
                            navController.navigate(Routes.Materias)
                        }
                    }
                },
                enabled = nombre.isNotBlank() && rolSeleccionado != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Ingresar")
            }
        }
    }

}
