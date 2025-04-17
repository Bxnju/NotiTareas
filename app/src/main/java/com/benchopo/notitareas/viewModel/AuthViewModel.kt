package com.benchopo.notitareas.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.data.model.Usuario

class AuthViewModel : ViewModel() {
    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    fun login(nombre: String, rol: Rol, usuariosViewModel: UsuariosViewModel): String? {
        // Buscar si ya existe el usuario
        val existente = usuariosViewModel.usuarios.find { it.nombre.contentEquals(nombre, ignoreCase = true) && it.rol == rol }

        if (existente == null) {
            return "No existe un usuario con ese nombre y rol."
        }else{
            usuarioActual = existente
            return null
        }

    }

    fun register(nombre: String, rol: Rol, usuariosViewModel: UsuariosViewModel): String? {
        // Buscar si ya existe el usuario
        val existente = usuariosViewModel.usuarios.find { it.nombre == nombre && it.rol == rol }

        if (existente != null) {
            return "Ya existe un usuario con ese nombre y rol."
        }else{
            usuarioActual = Usuario(nombre = nombre, rol = rol).also {
                usuariosViewModel.agregarUsuario(it)
            }
            return null
        }
    }

    fun logout() {
        usuarioActual = null
    }
}
