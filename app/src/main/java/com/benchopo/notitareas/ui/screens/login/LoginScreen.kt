package com.benchopo.notitareas.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.benchopo.notitareas.R
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.ui.components.Snackbar
import com.benchopo.notitareas.ui.components.rememberSnackbarHostState
import com.benchopo.notitareas.viewModel.AuthViewModel
import com.benchopo.notitareas.viewModel.UsuariosViewModel
import com.benchopo.notitareas.ui.navigation.Routes

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    usuariosViewModel: UsuariosViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
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
                .padding(top = 50.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Logo NotiTareas",
                    modifier = Modifier.size(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Tu nombre") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Tu contraseña") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { rolSeleccionado = Rol.PROFESOR },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rolSeleccionado == Rol.PROFESOR)
                            MaterialTheme.colorScheme.primary
                        else Color(0xFF252525),
                        contentColor = Color.White
                    )
                ) {
                    Text("Profesor")
                }
                Button(
                    onClick = { rolSeleccionado = Rol.ESTUDIANTE },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rolSeleccionado == Rol.ESTUDIANTE)
                            MaterialTheme.colorScheme.primary
                        else Color(0xFF252525),
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
                        val error = authViewModel.login(
                            nombre,
                            password,
                            rolSeleccionado!!,
                            usuariosViewModel
                        )
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿No tienes cuenta?",
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = { navController.navigate(Routes.Register) }
                ) {
                    Text(
                        text = "Regístrate aquí",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }
    }
}
