package com.benchopo.notitareas.viewModel

import androidx.lifecycle.ViewModel
import com.benchopo.notitareas.controller.UsuariosController
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.data.model.Usuario

class UsuariosViewModel : ViewModel() {



    private val _usuarios = mutableListOf<Usuario>()
    var usuarios = _usuarios

    init {
        UsuariosController().getUsuarios { usuariosFirestore ->
            _usuarios.clear()
            _usuarios.addAll(usuariosFirestore)
            usuarios = _usuarios
        }
    }



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
