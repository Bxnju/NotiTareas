package com.benchopo.notitareas.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.benchopo.notitareas.controller.MateriasController
import com.benchopo.notitareas.data.model.Materia

class MateriasViewModel : ViewModel() {
    private val _materias = mutableStateListOf<Materia>()
    val materias: List<Materia> get() = _materias

    init {
        MateriasController().getMaterias { materiasCargadas ->
            _materias.clear()
            _materias.addAll(materiasCargadas)
        }
    }


    fun agregarMateria(titulo: String, idProfesor: String, estudiantes: MutableList<String>): String? {
        if (titulo.length > 35) return "El titulo no puede tener más de 35 caracteres."
        if (_materias.any { it.titulo.equals(titulo, ignoreCase = true) }) {
            return "Ya existe una materia con ese titulo."
        }
        if (titulo.isBlank()) return "El titulo no puede estar vacío."

        //agregar materia
        MateriasController().setMateria(titulo, idProfesor, estudiantes)

        //asignar las materias a la lista de materias
        MateriasController().getMaterias { materiasCargadas ->
            _materias.clear()
            _materias.addAll(materiasCargadas)

        }
        return null
    }

    fun eliminarMateria(id: String) {
        MateriasController().deleteMateria(id) { result ->
            if (result) {
                _materias.removeIf { it.id == id }
            } else {
                Log.w("ViewModel", "No se pudo eliminar la materia con id: $id")
            }
        }
    }
}


