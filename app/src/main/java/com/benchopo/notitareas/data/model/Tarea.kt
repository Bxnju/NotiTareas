package com.benchopo.notitareas.data.model

import java.util.UUID

data class Tarea(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val fechaEntrega: String,
    val materia: String,
    val idMateria: String,
    val completadaPor: MutableList<String> = mutableListOf(),
)

