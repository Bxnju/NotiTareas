package com.benchopo.notitareas.data.model

import java.util.UUID

data class Materia(
    val id: String,
    val titulo: String,
    val idProfesor: String,
    val idEstudiantesInscritos: MutableList<String> = mutableListOf()
)
