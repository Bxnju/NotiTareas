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

    fun login(nombre: String, password: String, rol: Rol, usuariosViewModel: UsuariosViewModel): String? {
        // Buscar si ya existe el usuario
        val existente = usuariosViewModel.usuarios.find { it.nombre.trim().contentEquals(nombre.trim(), ignoreCase = true) && it.password == password && it.rol == rol }

        if (existente == null) {
            return "No existe un usuario con esas credenciales y ese rol."
        }else{
            usuarioActual = existente
            return null
        }

    }

    fun register(id : String, nombre: String, email: String, password: String, rol: Rol, usuariosViewModel: UsuariosViewModel): String? {
        // Buscar si ya existe el usuario
        val existente = usuariosViewModel.usuarios.find { it.nombre.contentEquals(nombre, ignoreCase = true) && it.rol == rol }

        if (existente != null) {
            return "Ya existe un usuario con ese nombre y rol."
        }else{
            usuarioActual = Usuario(id = id, nombre = nombre, email = email, password = password, rol = rol).also {
                usuariosViewModel.agregarUsuario(it)
            }
            return null
        }
    }

    fun logout() {
        usuarioActual = null
    }
}
