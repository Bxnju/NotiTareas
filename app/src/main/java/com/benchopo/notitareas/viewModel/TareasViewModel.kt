package com.benchopo.notitareas.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.benchopo.notitareas.data.model.Tarea

class TareasViewModel : ViewModel() {

    private val _tareas = mutableStateListOf<Tarea>()
    val tareas: List<Tarea> get() = _tareas

    fun agregarTarea(tarea: Tarea) {
        _tareas.add(tarea)
    }
}
