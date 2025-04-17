package com.benchopo.notitareas.ui.screens.materias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.viewModel.MateriasViewModel
import com.benchopo.notitareas.ui.components.Snackbar
import com.benchopo.notitareas.ui.components.rememberSnackbarHostState
import com.benchopo.notitareas.ui.components.AppTitle
import com.benchopo.notitareas.viewModel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriasScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    materiasViewModel: MateriasViewModel = viewModel(),
    onNavigateToTareas: () -> Unit
) {
    var materia by remember { mutableStateOf("") }
    val materias = materiasViewModel.materias

    val usuarioActual = authViewModel.usuarioActual

    val snackbarHostState = rememberSnackbarHostState()
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    @Composable
    fun EmptyMateriasMessage() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay materias registradas.", color = Color.Gray)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF180042), Color(0xFF340026)),
                    start = Offset.Zero,
                    end = Offset.Infinite
                ),
            )

    ) {
        Snackbar(snackbarHostState)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppTitle(usuarioActual!!.nombre)


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (usuarioActual.rol == Rol.PROFESOR) Text(
                    "Registrar Materias",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                ) else Text(
                    "Mis Materias",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )

                TextButton(onClick = onNavigateToTareas) {
                    Text("Ir a Tareas")
                }

            }

            if (usuarioActual.rol == Rol.PROFESOR) {
                OutlinedTextField(
                    value = materia,
                    onValueChange = { materia = it },
                    label = { Text("Nombre de la materia") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    "Añade estudiantes a la materia",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                //TODO: Agregar lista de estudiantes a la materia

                Button(
                    onClick = {
                        val error = materiasViewModel.agregarMateria(materia, usuarioActual.id)
                        if (error != null) {
                            snackbarMessage = error
                        } else {
                            snackbarMessage = "Materia agregada exitosamente."
                            materia = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Agregar")
                }
            }

            HorizontalDivider(thickness = 3.dp, color = Color.LightGray)

            Text("Materias registradas:", style = MaterialTheme.typography.titleMedium)

            if (materias.isEmpty()) {
                EmptyMateriasMessage()
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    materias.forEach { materiaItem ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(20.dp)),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            onClick = {
                                navController.navigate("tareasPorMateria/${materiaItem.titulo}")
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFF845EC2), Color(0xFFD65DB1))
                                        ),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(18.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = materiaItem.titulo,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                IconButton(
                                    onClick = {
                                        materiasViewModel.eliminarMateria(materiaItem.id)
                                        snackbarMessage = "Materia eliminada."
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color(0x40FFFFFF), CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
