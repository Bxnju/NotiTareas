package com.benchopo.notitareas.ui.screens.materias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.benchopo.notitareas.viewModel.MateriasViewModel
import com.benchopo.notitareas.ui.components.Snackbar
import com.benchopo.notitareas.ui.components.rememberSnackbarHostState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.benchopo.notitareas.ui.components.AppTitle

@Composable
fun MateriasScreen(
    materiasViewModel: MateriasViewModel = viewModel(),
    onNavigateToTareas: () -> Unit
) {
    var materia by remember { mutableStateOf("") }
    val materias = materiasViewModel.materias
    val snackbarHostState = rememberSnackbarHostState()
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    // Este LaunchedEffect lanza el snackbar flotante
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    @Composable
    fun EmptyMateriasMessage(){
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center){
            Text("No hay materias registradas.")
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
            Text("Registrar Materias", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = materia,
                onValueChange = { materia = it },
                label = { Text("Nombre de la materia") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val error = materiasViewModel.agregarMateria(materia)
                    if (error != null) {
                        snackbarMessage = error
                    } else {
                        snackbarMessage = "Materia agregada exitosamente."
                        materia = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Agregar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onNavigateToTareas() }) {
                Text("Ir a Tareas")
            }

            Text("Materias registradas:", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (materias.isEmpty()) {
                   item{
                       EmptyMateriasMessage()
                   }
                }else{
                    items(materias) { materiaItem ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF7F52FF), Color(0xFFC669FF))
                                        ), shape = RoundedCornerShape(30.dp)
                                    )
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = materiaItem.nombre,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = {
                                    materiasViewModel.eliminarMateria(materiaItem.id)
                                    snackbarMessage = "Materia eliminada."
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MateriasScreenPreview() {}
