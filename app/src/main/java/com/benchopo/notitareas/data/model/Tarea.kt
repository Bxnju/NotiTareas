package com.benchopo.notitareas.data.model

import java.util.UUID

data class Tarea(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descripcion: String,
    val fechaEntrega: String,
    val materia: String,
    var completada: Boolean = false
)

