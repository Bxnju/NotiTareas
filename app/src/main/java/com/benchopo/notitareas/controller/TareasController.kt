package com.benchopo.notitareas.controller

import android.util.Log
import com.benchopo.notitareas.data.model.Tarea
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class TareasController {
    private val db = Firebase.firestore
    private val tareasRef = db.collection("tareas")
    
    fun setTarea(tarea: Tarea, callback: (Boolean) -> Unit) {
        val tareaMap = hashMapOf(
            "titulo" to tarea.titulo,
            "descripcion" to tarea.descripcion,
            "fechaEntrega" to tarea.fechaEntrega,
            "materia" to tarea.materia,
            "idMateria" to tarea.idMateria,
            "completadaPor" to tarea.completadaPor
        )

        tareasRef.add(tareaMap)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener {
                Log.w("Firestore", "Error al agregar tarea", it)
                callback(false)
            }
    }

    fun deleteTarea(idTarea: String, callback: (Boolean) -> Unit) {
        tareasRef
            .document(idTarea).
            delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener {
                Log.w("Firestore", "Error al eliminar tarea", it)
                callback(false)
            }
    }

    fun getTareas(callback: (List<Tarea>) -> Unit) {
        tareasRef.get()
            .addOnSuccessListener { result ->
                val tareas = result.mapNotNull { document ->
                    val titulo = document.getString("titulo") ?: return@mapNotNull null
                    val descripcion = document.getString("descripcion") ?: ""
                    val fechaEntrega = document.getString("fechaEntrega") ?: ""
                    val materia = document.getString("materia") ?: ""
                    val idMateria = document.getString("idMateria") ?: ""
                    val completadaPor = document.get("completadaPor") as? List<String> ?: emptyList()

                    Tarea(
                        id = document.id,
                        titulo = titulo,
                        descripcion = descripcion,
                        fechaEntrega = fechaEntrega,
                        materia = materia,
                        idMateria = idMateria,
                        completadaPor = completadaPor.toMutableList()
                    )
                }
                callback(tareas)
            }
            .addOnFailureListener {
                Log.w("Firestore", "Error al obtener tareas", it)
                callback(emptyList())
            }
    }
}