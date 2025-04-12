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

    fun login(nombre: String, rol: Rol) {
        usuarioActual = Usuario(nombre = nombre, rol = rol)
    }

    fun logout() {
        usuarioActual = null
    }
}
