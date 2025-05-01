package com.benchopo.notitareas.controller

import android.content.ContentValues.TAG
import android.util.Log
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.data.model.Usuario
import com.benchopo.notitareas.viewModel.UsuariosViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class UsuariosController {
    private val db = Firebase.firestore
    private val usersRef = db.collection("users")


    fun getUsers(nombre: String, password: String, rol: Rol, callback: (Usuario?) -> Unit) {
        usersRef
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nombredb = document.getString("nombre")?.trim()
                    val passworddb = document.getString("password")
                    val roldb = document.getString("rol")

                    if (nombredb == nombre.trim() && passworddb == password && roldb == rol.toString()) {
                        val usuario = Usuario(
                            id = document.id,
                            nombre = nombredb,
                            email = document.getString("email") ?: "",
                            password = passworddb ?: "",
                            rol = Rol.valueOf(roldb)
                        )
                        callback(usuario)
                        return@addOnSuccessListener
                    }
                }
                callback(null)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error obteniendo los documentos.", exception)
                callback(null)
            }
    }

    fun getUsers(nombre: String, email: String,password: String, rol: Rol, callback: (Usuario?) -> Unit) {
        usersRef
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nombredb = document.getString("nombre")?.trim()
                    val passworddb = document.getString("password")
                    val roldb = document.getString("rol")
                    val emaildb = document.getString("email")

                    if (nombredb == nombre.trim() && passworddb == password && roldb == rol.toString() && emaildb == email) {
                        val usuario = Usuario(
                            id = document.id,
                            nombre = nombredb,
                            email = emaildb,
                            password = passworddb ?: "",
                            rol = Rol.valueOf(roldb)
                        )
                        callback(usuario)
                        return@addOnSuccessListener
                    }
                }
                callback(null)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error obteniendo los documentos.", exception)
                callback(null)
            }
    }

    fun getUsuarios(callback: (List<Usuario>) -> Unit) {
        usersRef
            .get()
            .addOnSuccessListener { result ->
                val listaUsuarios = result.mapNotNull { doc ->
                    val id = doc.id
                    val nombre = doc.getString("nombre") ?: return@mapNotNull null
                    val email = doc.getString("email") ?: ""
                    val password = doc.getString("password") ?: ""
                    val rolStr = doc.getString("rol") ?: return@mapNotNull null
                    val rol = try {
                        Rol.valueOf(rolStr.uppercase())
                    } catch (e: IllegalArgumentException) {
                        return@mapNotNull null
                    }
                    Usuario(id, nombre, email, password, rol)
                }
                callback(listaUsuarios)
            }
            .addOnFailureListener {
                callback(emptyList()) // Error: devuelve lista vacía
            }
    }

    fun setUser(nombre: String, email: String, password: String, rol: Rol){

        val user = hashMapOf(
            "nombre" to nombre,
            "password" to password,
            "rol" to rol.toString(),
            "email" to email
        )

        // Add a new document with a generated ID
        usersRef
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                UsuariosViewModel().agregarUsuario(Usuario(id = documentReference.id, nombre = nombre, email = email, password = password, rol = rol))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

}

