package com.benchopo.notitareas.ui.screens.tareas

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.benchopo.notitareas.data.model.Tarea
import com.benchopo.notitareas.viewModel.MateriasViewModel
import com.benchopo.notitareas.viewModel.TareasViewModel
import com.benchopo.notitareas.ui.components.DropdownMenuBox
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import com.benchopo.notitareas.ui.components.Snackbar
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.benchopo.notitareas.ui.components.AppTitle

@Composable
fun TareasScreen(
    tareasViewModel: TareasViewModel = viewModel(),
    materiasViewModel: MateriasViewModel = viewModel()
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaEntrega by remember { mutableStateOf("") }
    var materiaSeleccionada by remember { mutableStateOf("") }

    val materiasNombres: List<String> = materiasViewModel.materias.map { it.nombre }
    val context = LocalContext.current
    val calendario = Calendar.getInstance()

    val snackbarHostState = remember { SnackbarHostState() }
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
                .padding(16.dp)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppTitle()

            Text("Registrar Tarea", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                val datePicker = DatePickerDialog(
                    context,
                    { _: DatePicker, year: Int, month: Int, day: Int ->
                        fechaEntrega = "$day/${month + 1}/$year"
                    },
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
            }) {
                Text(if (fechaEntrega.isEmpty()) "Seleccionar fecha" else fechaEntrega)
            }

            DropdownMenuBox(
                items = materiasNombres,
                selectedItem = materiaSeleccionada,
                onItemSelected = { materiaSeleccionada = it }
            )

            Button(
                onClick = {
                    val tarea = Tarea(
                        titulo = titulo,
                        descripcion = descripcion,
                        fechaEntrega = fechaEntrega,
                        materia = materiaSeleccionada
                    )
                    tareasViewModel.agregarTarea(tarea)
                    titulo = ""
                    descripcion = ""
                    fechaEntrega = ""
                    materiaSeleccionada = ""
                },
                enabled = titulo.isNotBlank() && materiaSeleccionada.isNotBlank()
            ) {
                Text("Guardar Tarea")
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text("Tareas registradas:", style = MaterialTheme.typography.titleMedium)

            val materiasDisponibles = tareasViewModel.obtenerMateriasUnicas()
            val materiasFiltradas = tareasViewModel.materiasFiltradas
            val tareasFiltradas = tareasViewModel.obtenerTareasFiltradas()
            val ordenarPorFechaAsc = tareasViewModel.ordenarPorFechaAscendente.value

            Text("Filtrar por materia:", style = MaterialTheme.typography.titleMedium)
            if (materiasDisponibles.isEmpty()) {
                Text("No hay filtros disponibles.")
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(materiasDisponibles) { materia ->
                    AssistChip(
                        onClick = { tareasViewModel.alternarFiltroMateria(materia) },
                        label = { Text(materia) },
                        colors = AssistChipDefaults.assistChipColors(
                            // Color con gradiente cuando esta seleccionada
                            containerColor = if (materiasFiltradas.contains(materia)) Color(
                                0xFF5629D9
                            ) else Color.DarkGray,
                            labelColor = Color.White
                        )
                    )
                }
            }

            Button(
                onClick = { tareasViewModel.toggleOrdenPorFecha() },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(if (ordenarPorFechaAsc) "Quitar orden por fecha" else "Ordenar por fecha más próxima")
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tareasFiltradas) { tarea ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = if (tarea.completada)
                                        Brush.linearGradient(
                                            listOf(
                                                Color(0xFF0063B6),
                                                Color(0xFF04754C)
                                            )
                                        )
                                    else
                                        Brush.linearGradient(
                                            listOf(
                                                Color(0xFF888888),
                                                Color(0xFFFF5722)
                                            )
                                        )
                                )
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = tarea.titulo,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Materia: ${tarea.materia}", color = Color.White)
                                Text(text = "Entrega: ${tarea.fechaEntrega}", color = Color.White)
                                if (tarea.descripcion.isNotBlank()) {
                                    Text(
                                        text = "Descripción: ${tarea.descripcion}",
                                        color = Color.White
                                    )
                                }
                            }

                            IconButton(onClick = {
                                tareasViewModel.eliminarTarea(tarea)
                                snackbarMessage = "Tarea eliminada correctamente"
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar tarea",
                                    tint = Color.White
                                )
                            }

                            IconButton(
                                onClick = {
                                    tareasViewModel.marcarComoCompletada(tarea)
                                    snackbarMessage = "Tarea marcada como completada 🎉"
                                },
                                enabled = !tarea.completada
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Marcar como completada",
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
