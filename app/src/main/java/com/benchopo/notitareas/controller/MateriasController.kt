package com.benchopo.notitareas.controller

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.core.location.GnssStatusCompat.Callback
import com.benchopo.notitareas.data.model.Materia
import com.benchopo.notitareas.data.model.Rol
import com.benchopo.notitareas.data.model.Usuario
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MateriasController {
    private val db = Firebase.firestore
    private val materiasRef = db.collection("materias")

    fun setMateria(titulo: String, idProfesor: String, estudiantes: MutableList<String>){

        val materia = hashMapOf(
            "titulo" to titulo,
            "idProfesor" to idProfesor,
            "idEstudiantesInscritos" to estudiantes
        )

        // Add a new document with a generated ID
        materiasRef
            .add(materia)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun getMaterias(callback: (List<Materia>) -> Unit){
        materiasRef
            .get()
            .addOnSuccessListener { result ->
                val materias = mutableListOf<Materia>()
                for (document in result) {
                    val titulo = document.getString("titulo")
                    val idProfesor = document.getString("idProfesor")
                    val estudiantes = document.get("idEstudiantesInscritos") as? List<String>

                    if (titulo != null && idProfesor != null && estudiantes != null) {
                        val materia = Materia(
                            id = document.id,
                            titulo = titulo,
                            idProfesor = idProfesor,
                            idEstudiantesInscritos = estudiantes.toMutableList(),
                        )
                        materias.add(materia)
                    }
                }
                callback(materias)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error obteniendo los documentos.", exception)
                callback(emptyList())
            }

    }

    fun deleteMateria(id: String,  callback: (Boolean) -> Unit){
        materiasRef
            .document(id)
            .delete()
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error eliminando materia", e)
                callback(false)
            }
    }
}