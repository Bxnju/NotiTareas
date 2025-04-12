package com.benchopo.notitareas.data.model

import java.util.UUID

data class Usuario(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val rol: Rol,
    val materiasInscritas: MutableList<String> = mutableListOf()
)

enum class Rol {
    PROFESOR,
    ESTUDIANTE
}
