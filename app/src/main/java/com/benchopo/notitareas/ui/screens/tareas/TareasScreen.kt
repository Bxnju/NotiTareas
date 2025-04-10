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

@Composable
fun TareasScreen(
    tareasViewModel: TareasViewModel = viewModel(),
    materiasViewModel: MateriasViewModel = viewModel()
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaEntrega by remember { mutableStateOf("") }
    var materiaSeleccionada by remember { mutableStateOf("") }

    val materias = materiasViewModel.materias
    val context = LocalContext.current

    val calendario = Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Titulo de la app

        Text("NotiTareas 😎✔", style = MaterialTheme.typography.headlineLarge)


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
            items = materias,
            selectedItem = materiaSeleccionada,
            onItemSelected = { materiaSeleccionada = it }
        )

        Button(
            onClick = {
                val tarea = Tarea(titulo, descripcion, fechaEntrega, materiaSeleccionada)
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

        Spacer(modifier = Modifier.height(16.dp))

        Text("Tareas registradas:", style = MaterialTheme.typography.titleMedium)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tareasViewModel.tareas.forEach { tarea ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(Color(0xFF52B2FF), Color(0xFF69FFC6))
                                )
                            )
                            .padding(16.dp).fillMaxWidth(),
                    ) {
                        Text(text = tarea.titulo, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Materia: ${tarea.materia}", color = Color.White)
                        Text(text = "Entrega: ${tarea.fechaEntrega}", color = Color.White)
                        if (tarea.descripcion.isNotBlank()) {
                            Text(text = "Descripción: ${tarea.descripcion}", color = Color.White)
                        }
                    }
                }
            }
        }

    }
}
