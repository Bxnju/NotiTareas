package com.benchopo.notitareas.ui.screens.materias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.benchopo.notitareas.data.model.Materia
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.viewModel.MateriasViewModel
import com.benchopo.notitareas.ui.components.Snackbar
import com.benchopo.notitareas.ui.components.rememberSnackbarHostState
import com.benchopo.notitareas.ui.components.AppTitle
import com.benchopo.notitareas.viewModel.AuthViewModel
import com.benchopo.notitareas.viewModel.UsuariosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriasScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    materiasViewModel: MateriasViewModel = viewModel(),
    usuariosViewModel: UsuariosViewModel = viewModel(),
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

    fun Modifier.fadingEdge(brush: Brush) = this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.DstIn)
        }

    // Main screen content

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
                horizontalArrangement = if (usuarioActual.rol == Rol.PROFESOR) Arrangement.SpaceBetween else Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (usuarioActual.rol == Rol.PROFESOR) {

                    Text(
                        "Registrar Materias",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold

                    )

                    TextButton(onClick = onNavigateToTareas) {
                        Text("Ir a Tareas")
                    }
                } else Text(
                    "Mis Materias",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )


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

                var textSearch by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = textSearch,
                    onValueChange = { textSearch = it },
                    label = { Text("Buscar estudiante") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                )

                var estudiantesParaInscribir by remember { mutableStateOf(mutableListOf<String>()) }
                var resultadoEstudiantes = usuariosViewModel.buscarEstudiantesPorNombre(textSearch)

                if (resultadoEstudiantes.isNotEmpty()) {

                    val topFade = Brush.verticalGradient(0f to Color.Transparent, 0.3f to Color.Red)
                    val bottomFade = Brush.verticalGradient(0.7f to Color.Red, 1f to Color.Transparent)
                    val topBottomFade = Brush.verticalGradient(0f to Color.Transparent, 0.1f to Color.Red, 0.7f to Color.Red, 1f to Color.Transparent)
                    val leftRightFade = Brush.horizontalGradient(0f to Color.Transparent, 0.1f to Color.Red, 0.9f to Color.Red, 1f to Color.Transparent)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(vertical = 10.dp)
                            .fadingEdge(topBottomFade)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp),

                        ) {
                        resultadoEstudiantes.forEach { estudiante ->

                            var estudianteSeleccionado by remember { mutableStateOf(false) }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.horizontalGradient(
                                            if (estudianteSeleccionado) listOf(
                                                Color(0xFF845EC2),
                                                Color(0xFFD65DB1)
                                            ) else listOf(
                                                Color(0xFF4C1B93),
                                                Color(0xFFAD187F)
                                            )
                                        ),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(vertical = 10.dp)
                                    .padding(horizontal = 10.dp)
                                    .clickable(onClick = {
                                        if (estudiantesParaInscribir.contains(estudiante.id)) {
                                            estudiantesParaInscribir.remove(estudiante.id)
                                            estudianteSeleccionado = false
                                            snackbarMessage = "Estudiante ${estudiante.nombre} se removió."
                                        } else {
                                            estudiantesParaInscribir.add(estudiante.id)
                                            estudianteSeleccionado = true
                                            snackbarMessage = "Estudiante ${estudiante.nombre} se agregó."
                                        }
                                    }),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            ) {
                                Text(
                                    estudiante.nombre,
                                    modifier = Modifier.padding(10.dp),
                                    color = Color.White
                                )
                            }

                        }
                    }

                } else {
                    Text("No se encontraron resultados.")
                }

                Button(
                    onClick = {
                        val error = materiasViewModel.agregarMateria(
                            materia,
                            usuarioActual.id,
                            estudiantesParaInscribir
                        )
                        if (error != null) {
                            snackbarMessage = error
                        } else {
                            snackbarMessage =
                                "Materia agregada exitosamente."
                            materia = ""
                            textSearch = ""
                            estudiantesParaInscribir = mutableListOf()
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

                var materiasPorEstudiante = mutableListOf<Materia>()

                if (usuarioActual.rol == Rol.ESTUDIANTE) {

                    materias.forEach { materia ->
                        if (materia.idEstudiantesInscritos.contains(usuarioActual.id)) {
                            materiasPorEstudiante.add(materia)
                        }
                    }

                } else materiasPorEstudiante = materias as MutableList<Materia>

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    materiasPorEstudiante.forEach { materiaItem ->
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
