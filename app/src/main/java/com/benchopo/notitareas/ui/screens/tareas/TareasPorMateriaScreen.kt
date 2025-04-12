package com.benchopo.notitareas.ui.screens.tareas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import com.benchopo.notitareas.data.model.Tarea
import com.benchopo.notitareas.viewModel.TareasViewModel
import com.benchopo.notitareas.ui.components.Snackbar
import com.benchopo.notitareas.ui.components.rememberSnackbarHostState
import com.benchopo.notitareas.ui.components.AppTitle

@Composable
fun TareasPorMateriaScreen(
    navController: NavController,
    nombreMateria: String,
    tareasViewModel: TareasViewModel = viewModel()
) {
    val tareasFiltradas = remember(tareasViewModel.tareas, tareasViewModel.ordenarPorFechaAscendente.value) {
        tareasViewModel.tareas
            .filter { it.materia == nombreMateria }
            .let {
                if (tareasViewModel.ordenarPorFechaAscendente.value) {
                    it.sortedBy { tarea -> tarea.fechaEntrega }
                } else it
            }
    }

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
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppTitle()

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
                        Card(
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        brush = if (tarea.completada)
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
                                    Text(tarea.titulo, color = Color.White, fontWeight = FontWeight.Bold)
                                    Text("Materia: ${tarea.materia}", color = Color.White)
                                    Text("Entrega: ${tarea.fechaEntrega}", color = Color.White)
                                    if (tarea.descripcion.isNotBlank()) {
                                        Text("Descripción: ${tarea.descripcion}", color = Color.White)
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    IconButton(onClick = {
                                        tareasViewModel.eliminarTarea(tarea)
                                        snackbarMessage = "Tarea eliminada correctamente"
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
                                    }

                                    IconButton(
                                        onClick = {
                                            if (!tarea.completada) snackbarMessage = "Tarea marcada como completada 🎉"
                                            if (!tarea.completada) tareasViewModel.marcarComoCompletada(tarea)
                                            else tareasViewModel.marcarComoIncompleta(tarea)
                                        },
                                    ) {
                                        Icon(if (tarea.completada) Icons.Default.Clear else Icons.Default.Check,
                                            contentDescription = "Completar",
                                            tint = Color.White)
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
