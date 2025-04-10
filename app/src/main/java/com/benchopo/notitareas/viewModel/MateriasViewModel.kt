package com.benchopo.notitareas.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.benchopo.notitareas.data.model.Materia

class MateriasViewModel : ViewModel() {
    private val _materias = mutableStateListOf<Materia>()
    val materias: List<Materia> get() = _materias

    fun agregarMateria(nombre: String): String? {
        if (nombre.length > 35) return "El nombre no puede tener más de 35 caracteres."
        if (_materias.any { it.nombre.equals(nombre, ignoreCase = true) }) {
            return "Ya existe una materia con ese nombre."
        }
        if (nombre.isBlank()) return "El nombre no puede estar vacío."

        _materias.add(Materia(nombre = nombre))
        return null // No hubo error
    }

    fun eliminarMateria(id: String) {
        _materias.removeIf { it.id == id }
    }
}


