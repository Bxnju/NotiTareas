package com.benchopo.notitareas.data.model
import java.util.UUID

data class Materia(val id: String = UUID.randomUUID().toString(), val nombre: String)
