package com.benchopo.notitareas.ui.screens.materias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.benchopo.notitareas.ui.theme.NotiTareasTheme
import com.benchopo.notitareas.viewModel.MateriasViewModel

@Composable
fun MateriasScreen(materiasViewModel: MateriasViewModel  = viewModel(),
                   onNavigateToTareas: () -> Unit) {
    var materia by remember { mutableStateOf("") }

    val materias = materiasViewModel.materias

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Titulo de la app

        Text("NotiTareas 😎✔", style = MaterialTheme.typography.headlineLarge)

        Text("Registrar Materias", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = materia,
            onValueChange = { materia = it },
            label = { Text("Nombre de la materia") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                materiasViewModel.agregarMateria(materia)
                materia = ""
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Materias registradas:", style = MaterialTheme.typography.titleMedium)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            materiasViewModel.materias.forEach { materia ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(
                        modifier = Modifier.background(brush = Brush.linearGradient(listOf(Color(0xFF7F52FF), Color(0xFFC669FF))))
                            .padding(16.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = materia, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onNavigateToTareas() }) {
            Text("Ir a Tareas")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MateriasScreenPreview() {}
