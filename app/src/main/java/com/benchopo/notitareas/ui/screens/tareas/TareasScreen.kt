package com.benchopo.notitareas.ui.screens.tareas

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.benchopo.notitareas.data.model.Tarea
import com.benchopo.notitareas.ui.components.AppTitle
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.ui.components.DropdownMenuBox
import com.benchopo.notitareas.ui.components.Snackbar
import com.benchopo.notitareas.viewModel.AuthViewModel
import com.benchopo.notitareas.viewModel.MateriasViewModel
import com.benchopo.notitareas.viewModel.TareasViewModel
import com.benchopo.notitareas.viewModel.UsuariosViewModel
import java.util.*

@Composable
fun TareasScreen(
    navController: NavController,
    tareasViewModel: TareasViewModel = viewModel(),
    materiasViewModel: MateriasViewModel = viewModel(),
    authViewModel: AuthViewModel,
    usuariosViewModel: UsuariosViewModel
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaEntrega by remember { mutableStateOf("") }
    var materiaSeleccionada by remember { mutableStateOf("") }

    val usuarioActual = authViewModel.usuarioActual
    val materiasNombres = materiasViewModel.obtenerMateriaPorIdUsuario(usuarioActual!!.id).map { it.titulo }
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

    @Composable
    fun EmptyTareasMessage() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay tareas registradas.", color = Color.Gray)
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppTitle(navController, authViewModel, usuarioActual!!.nombre)

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            if (usuarioActual.rol == Rol.PROFESOR) {
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

                Button(
                    onClick = {
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
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (fechaEntrega.isEmpty()) "Seleccionar fecha" else "Fecha: $fechaEntrega")
                }

                DropdownMenuBox(
                    items = materiasNombres,
                    selectedItem = materiaSeleccionada,
                    onItemSelected = { materiaSeleccionada = it }
                )

                Button(
                    onClick = {
                        val materia =
                            materiasViewModel.materias.find { it.titulo == materiaSeleccionada }
                        val tarea = Tarea(
                            id = (tareasViewModel.tareas.size + 1).toString(),
                            titulo = titulo,
                            descripcion = descripcion,
                            fechaEntrega = fechaEntrega,
                            materia = materiaSeleccionada,
                            idMateria = materia?.id!!
                        )
                        val error = tareasViewModel.agregarTarea(tarea)
                        if (error != null) {
                            snackbarMessage = error
                        } else {
                            snackbarMessage = "Tarea agregada exitosamente."
                            titulo = ""
                            descripcion = ""
                            fechaEntrega = ""
                            materiaSeleccionada = ""
                        }
                    },
                    enabled = titulo.isNotBlank() && materiaSeleccionada.isNotBlank(),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Guardar Tarea")
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 3.dp,
                    color = Color.LightGray
                )
            }

            Text("Filtrar por materia:", style = MaterialTheme.typography.titleMedium)

            val materiasDisponibles = tareasViewModel.obtenerMateriasUnicas()
            val materiasFiltradas = tareasViewModel.materiasFiltradas
            val tareasFiltradas = tareasViewModel.obtenerTareasFiltradas()
            val ordenarPorFechaAsc = tareasViewModel.ordenarPorFechaAscendente.value

            if (materiasDisponibles.isEmpty()) {
                Text("No hay filtros disponibles.")
            } else {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    materiasDisponibles.forEach { materia ->
                        AssistChip(
                            onClick = { tareasViewModel.alternarFiltroMateria(materia) },
                            label = { Text(materia) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (materiasFiltradas.contains(materia)) Color(
                                    0xFF5629D9
                                )
                                else Color(0xFF252525),
                                labelColor = Color.White
                            )
                        )
                    }
                }
            }

            Button(
                onClick = { tareasViewModel.toggleOrdenPorFecha() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !materiasFiltradas.isEmpty()
            ) {
                Text(if (ordenarPorFechaAsc) "Quitar orden por fecha" else "Ordenar por fecha más próxima")
            }

            Text("Tareas registradas:", style = MaterialTheme.typography.titleMedium)

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var tareasPorProfesor = mutableListOf<Tarea>()
                var materiasPorProfesor =
                    materiasViewModel.obtenerMateriaPorIdUsuario(usuarioActual.id)

                tareasFiltradas.forEach { tarea ->
                    if (tarea.idMateria in materiasPorProfesor.map { it.id }) {
                        tareasPorProfesor.add(tarea)
                    }
                }

                if (tareasPorProfesor.isEmpty()) {
                    EmptyTareasMessage()
                } else {
                    tareasPorProfesor.forEach { tarea ->

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

