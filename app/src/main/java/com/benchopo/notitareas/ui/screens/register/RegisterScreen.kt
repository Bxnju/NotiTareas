package com.benchopo.notitareas.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.benchopo.notitareas.ui.components.Snackbar
import com.benchopo.notitareas.ui.components.rememberSnackbarHostState
import com.benchopo.notitareas.viewModel.AuthViewModel
import com.benchopo.notitareas.viewModel.UsuariosViewModel
import com.benchopo.notitareas.data.model.Rol
import java.util.*

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    usuariosViewModel: UsuariosViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf<Rol?>(null) }
    val snackbarHostState = rememberSnackbarHostState()
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF180042))
    ) {
        Snackbar(snackbarHostState)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Registrarse",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { rolSeleccionado = Rol.PROFESOR },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rolSeleccionado == Rol.PROFESOR) MaterialTheme.colorScheme.primary else Color(
                            0xFF252525
                        ),
                        contentColor = Color.White
                    )
                ) {
                    Text("Profesor")
                }
                Button(
                    onClick = { rolSeleccionado = Rol.ESTUDIANTE },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rolSeleccionado == Rol.ESTUDIANTE) MaterialTheme.colorScheme.primary else Color(
                            0xFF252525
                        ),
                        contentColor = Color.White
                    )
                ) {
                    Text("Estudiante")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.isNotBlank() && email.isNotBlank() && password.isNotBlank() && rolSeleccionado != null) {
                        val error = authViewModel.register(
                            id = (usuariosViewModel.usuarios.size + 1).toString(),
                            nombre = nombre,
                            email = email,
                            password = password,
                            rol = rolSeleccionado!!,
                            usuariosViewModel = usuariosViewModel
                        )
                        if (error != null) {
                            snackbarMessage = error
                        } else {
                            snackbarMessage = "Usuario registrado correctamente."
                            navController.navigate("login")
                        }
                    } else {
                        snackbarMessage = "Completa todos los campos."
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Registrar")
            }
        }
    }
}
