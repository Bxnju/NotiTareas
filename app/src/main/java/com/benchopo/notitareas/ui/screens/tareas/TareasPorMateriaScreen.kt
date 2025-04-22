package com.benchopo.notitareas.ui.screens.tareas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.viewModel.TareasViewModel
import com.benchopo.notitareas.ui.components.Snackbar
import com.benchopo.notitareas.ui.components.rememberSnackbarHostState
import com.benchopo.notitareas.ui.components.AppTitle
import com.benchopo.notitareas.viewModel.AuthViewModel

@Composable
fun TareasPorMateriaScreen(
    navController: NavController,
    nombreMateria: String,
    authViewModel: AuthViewModel,
    tareasViewModel: TareasViewModel = viewModel()
) {
    val tareasFiltradas = tareasViewModel.tareas
        .filter { it.materia == nombreMateria }
        .let {
            if (tareasViewModel.ordenarPorFechaAscendente.value) {
                it.sortedBy { tarea -> tarea.fechaEntrega }
            } else it
        }

    val usuarioActual = authViewModel.usuarioActual

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
            .background(
                Color(0xFF180042)
            )
    ) {
        Snackbar(snackbarHostState)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppTitle(usuarioActual!!.nombre)

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tareas de $nombreMateria",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = { tareasViewModel.toggleOrdenPorFecha() },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ordenar por fecha")
            }

            if (tareasFiltradas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay tareas para esta materia", color = Color.Gray)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    tareasFiltradas.forEach { tarea ->

                        var completadaPorEstudiante = false
                        tarea.completadaPor.forEach {
                            if (it == usuarioActual.id) {
                                completadaPorEstudiante = true
                            }
                        }

                        Card(
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        brush = if (completadaPorEstudiante)
                                            Brush.linearGradient(
                                                listOf(Color(0xFF0063B6), Color(0xFF04754C))
                                            )
                                        else
                                            Brush.linearGradient(
                                                listOf(Color(0xFF888888), Color(0xFFFF5722))
                                            )
                                    )
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        tarea.titulo,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text("Materia: ${tarea.materia}", color = Color.White)
                                    Text("Entrega: ${tarea.fechaEntrega}", color = Color.White)
                                    if (tarea.descripcion.isNotBlank()) {
                                        Text(
                                            "Descripción: ${tarea.descripcion}",
                                            color = Color.White
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    if (usuarioActual.rol == Rol.PROFESOR) {
                                        IconButton(onClick = {
                                            tareasViewModel.eliminarTarea(tarea)
                                            snackbarMessage = "Tarea eliminada correctamente"
                                        }) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = Color.White
                                            )
                                        }
                                    }

                                    if (usuarioActual.rol == Rol.ESTUDIANTE) {
                                        IconButton(
                                            onClick = {
                                                if (!completadaPorEstudiante) {
                                                    snackbarMessage =
                                                        "Tarea marcada como completada 🎉"

                                                    tareasViewModel.marcarComoCompletada(
                                                        tarea, usuarioActual.id
                                                    )
                                                } else tareasViewModel.marcarComoIncompleta(
                                                    tarea,
                                                    usuarioActual.id
                                                )
                                            },
                                        ) {
                                            Icon(
                                                if (completadaPorEstudiante) Icons.Default.Clear else Icons.Default.Check,
                                                contentDescription = "Completar",
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
    }
}
