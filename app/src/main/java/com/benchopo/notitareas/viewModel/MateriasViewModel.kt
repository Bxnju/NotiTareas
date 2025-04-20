package com.benchopo.notitareas.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.benchopo.notitareas.data.model.Materia

class MateriasViewModel : ViewModel() {
    private val _materias = mutableStateListOf<Materia>()
    val materias: List<Materia> get() = _materias

    fun agregarMateria(titulo: String, idProfesor: String, estudiantes: MutableList<String>): String? {
        if (titulo.length > 35) return "El titulo no puede tener más de 35 caracteres."
        if (_materias.any { it.titulo.equals(titulo, ignoreCase = true) }) {
            return "Ya existe una materia con ese titulo."
        }
        if (titulo.isBlank()) return "El titulo no puede estar vacío."

        _materias.add(Materia((materias.size + 1).toString() ,titulo = titulo, idProfesor = idProfesor, idEstudiantesInscritos = estudiantes ))
        return null
    }

    fun eliminarMateria(id: String) {
        _materias.removeIf { it.id == id }
    }
}


