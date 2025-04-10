package com.benchopo.notitareas.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MateriasViewModel : ViewModel() {

    // Lista observable de materias
    private val _materias = mutableStateListOf<String>()
    val materias: List<String> get() = _materias

    // Agregar una nueva materia
    fun agregarMateria(nombre: String) {
        if (nombre.isNotBlank()) {
            _materias.add(nombre)
        }
    }
}
