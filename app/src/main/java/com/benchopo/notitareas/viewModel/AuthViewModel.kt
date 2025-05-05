package com.benchopo.notitareas.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.benchopo.notitareas.controller.TareasController
import com.benchopo.notitareas.controller.UsuariosController
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.data.model.Usuario

class AuthViewModel : ViewModel() {
    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    fun login(email: String, password: String, rol: Rol, callback: (String?) -> Unit) {
        UsuariosController().getUsers(email, password, rol) { usuarioEncontrado ->
            if (usuarioEncontrado == null) {
                callback(null)

            } else {
                usuarioActual = usuarioEncontrado
                callback("Usuario existente")
            }
        }
    }


    fun register(nombre: String, email: String, password: String, rol: Rol, callback: (String?) -> Unit) {

        //Usario existe?
        UsuariosController().getUsers(nombre, email, password, rol) { usuarioEncontrado ->
            if (usuarioEncontrado == null) {
                //No existe el usuario, crearlo
                UsuariosController().setUser(nombre, email, password, rol)
                callback("Usuario registrado correctamente.")
            } else {
                callback(null)

            }
        }


    }

    fun logout() {
        usuarioActual = null
    }
}
