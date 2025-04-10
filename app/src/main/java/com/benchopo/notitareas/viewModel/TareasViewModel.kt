package com.benchopo.notitareas.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.benchopo.notitareas.data.model.Tarea

class TareasViewModel : ViewModel() {
    private val _tareas = mutableStateListOf<Tarea>()
    val tareas: List<Tarea> get() = _tareas

    private val _materiasFiltradas = mutableStateListOf<String>()
    val materiasFiltradas: List<String> get() = _materiasFiltradas

    var ordenarPorFechaAscendente = mutableStateOf(false)
        private set

    fun agregarTarea(tarea: Tarea) {
        _tareas.add(tarea)
    }

    fun eliminarTarea(tarea: Tarea) {
        _tareas.remove(tarea)
    }

    fun marcarComoCompletada(tarea: Tarea) {
        val index = _tareas.indexOf(tarea)
        if (index != -1) {
            _tareas[index] = _tareas[index].copy(completada = true)
        }
    }

    fun alternarFiltroMateria(materia: String) {
        if (_materiasFiltradas.contains(materia)) {
            _materiasFiltradas.remove(materia)
        } else {
            _materiasFiltradas.add(materia)
        }
    }

    fun toggleOrdenPorFecha() {
        ordenarPorFechaAscendente.value = !ordenarPorFechaAscendente.value
    }

    fun obtenerTareasFiltradas(): List<Tarea> {
        var lista = if (_materiasFiltradas.isEmpty()) {
            _tareas
        } else {
            _tareas.filter { it.materia in _materiasFiltradas }
        }

        if (ordenarPorFechaAscendente.value) {
            lista = lista.sortedBy { it.fechaEntrega }
        }

        return lista
    }

    fun obtenerMateriasUnicas(): List<String> {
        return _tareas.map { it.materia }.distinct()
    }
}

