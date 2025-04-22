package com.benchopo.notitareas.data.model

data class Usuario(
    val id: String,
    val nombre: String,
    val email: String = "",
    val password: String = "",
    val rol: Rol
)

enum class Rol {
    PROFESOR,
    ESTUDIANTE
}
