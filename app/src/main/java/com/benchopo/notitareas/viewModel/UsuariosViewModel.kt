package com.benchopo.notitareas.viewModel

import androidx.lifecycle.ViewModel
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.data.model.Usuario

class UsuariosViewModel : ViewModel() {
    private val _usuarios = mutableListOf(
        Usuario(id = 1.toString(), nombre = "Ana", rol = Rol.ESTUDIANTE, email = "ana@notitareas.com", password = "pass"),
        Usuario(id = 2.toString(), nombre = "Luis", rol = Rol.ESTUDIANTE, email = "luis@notitareas.com", password = "pass"),
        Usuario(id = 3.toString(), nombre = "Carlos", rol = Rol.PROFESOR, email = "carlos@notitareas.com", password = "pass"),
        Usuario(id = 4.toString(), nombre = "Laura", rol = Rol.ESTUDIANTE, email = "laura@notitareas.com", password = "pass"),
        Usuario(id = 5.toString(), nombre = "Pedro", rol = Rol.PROFESOR, email = "pedro@notitareas.com", password = "pass"),
        Usuario(id = 6.toString(), nombre = "María", rol = Rol.ESTUDIANTE, email = "maria@notitareas.com", password = "pass"),
        Usuario(id = 7.toString(), nombre = "Juan", rol = Rol.PROFESOR, email = "juan@notitareas.com", password = "pass"),
    )
    var usuarios = _usuarios

//    fun buscarEstudiantes(): List<Usuario> {
//        return _usuarios.filter { it.rol == Rol.ESTUDIANTE }
//    }

    fun buscarEstudiantesPorNombre(nombre: String): List<Usuario> {
        return _usuarios.filter {
            it.rol == Rol.ESTUDIANTE && it.nombre.contains(nombre, ignoreCase = true)
        }
    }

//    fun obtenerUsuarioPorId(id: String): Usuario? {
//        return _usuarios.find { it.id == id }
//    }

    fun agregarUsuario(usuario: Usuario) {
        _usuarios.add(usuario)
        usuarios = _usuarios
    }

}
