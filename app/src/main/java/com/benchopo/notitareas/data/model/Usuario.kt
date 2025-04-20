package com.benchopo.notitareas.data.model

data class Usuario(
    val id: String,
    val nombre: String,
    val rol: Rol
)

enum class Rol {
    PROFESOR,
    ESTUDIANTE
}
