package com.benchopo.notitareas.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.benchopo.notitareas.controller.TareasController
import com.benchopo.notitareas.data.model.Tarea

class TareasViewModel : ViewModel() {
    private val _tareas = mutableStateListOf<Tarea>()
    val tareas: List<Tarea> get() = _tareas

    private val _materiasFiltradas = mutableStateListOf<String>()
    val materiasFiltradas: List<String> get() = _materiasFiltradas

    var ordenarPorFechaAscendente = mutableStateOf(false)
        private set


    init {
        TareasController().getTareas { tareasFirestore ->
            _tareas.clear()
            _tareas.addAll(tareasFirestore)
        }
    }


    fun agregarTarea(tarea: Tarea) : String? {
        if (_tareas.any { it.titulo == tarea.titulo } && _tareas.any { it.materia == tarea.materia }) {
            return "Ya existe una tarea con ese título en esa materia."
        }

        if (tarea.titulo.length > 35) return "El título no puede tener más de 35 caracteres."
        if (tarea.descripcion.length > 35) return "La descripción no puede tener más de 35 caracteres."

        TareasController().setTarea(tarea) { resultado ->
            if (resultado) {
                _tareas.add(tarea)
            } else {
                Log.w("TareasViewModel", "Error al agregar tarea a Firestore")
            }
        }
        return null
    }

    fun eliminarTarea(tarea: Tarea) {
        TareasController().deleteTarea(tarea.id) { resultado ->
            if (resultado) {
                _tareas.remove(tarea)
            } else {
                Log.w("TareasViewModel", "Error al eliminar tarea de Firestore")
            }
        }
    }

    fun marcarComoCompletada(tarea: Tarea, idUsuario: String) {
        val index = _tareas.indexOf(tarea)
        if (index != -1) {
            tarea.completadaPor.add(idUsuario)
            _tareas[index] = tarea.copy()
        }
    }

    fun marcarComoIncompleta(tarea: Tarea,  idUsuario: String) {
        val index = _tareas.indexOf(tarea)
        if (index != -1) {
            tarea.completadaPor.remove(idUsuario)
            _tareas[index] = tarea.copy()
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

