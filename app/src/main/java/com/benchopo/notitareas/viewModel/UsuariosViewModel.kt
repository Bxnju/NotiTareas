package com.benchopo.notitareas.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.data.model.Usuario

class UsuariosViewModel : ViewModel() {
    private val _usuarios = mutableStateListOf<Usuario>()
    val usuarios: List<Usuario> get() = _usuarios

    init {
        // Lista estática para pruebas
        _usuarios.addAll(
            listOf(
                Usuario(nombre = "Ana", rol = Rol.ESTUDIANTE),
                Usuario(nombre = "Luis", rol = Rol.ESTUDIANTE),
                Usuario(nombre = "Carlos", rol = Rol.PROFESOR)
            )
        )
    }

    fun buscarEstudiantes(): List<Usuario> {
        return _usuarios.filter { it.rol == Rol.ESTUDIANTE }
    }

    fun buscarEstudiantesPorNombre(nombre: String): List<Usuario> {
        return _usuarios.filter {
            it.rol == Rol.ESTUDIANTE && it.nombre.contains(nombre, ignoreCase = true)
        }
    }

    fun obtenerUsuarioPorId(id: String): Usuario? {
        return _usuarios.find { it.id == id }
    }

    fun agregarUsuario(usuario: Usuario) {
        _usuarios.add(usuario)
    }
}
