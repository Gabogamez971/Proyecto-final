package com.example.crud.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "citas",
    foreignKeys = [
        ForeignKey(
            entity = User::class, // Clase de entidad referenciada
            parentColumns = ["cedula"], // Campo en la tabla de usuarios que actúa como clave primaria
            childColumns = ["userCedula"], // Campo en la tabla de citas que actúa como clave foránea
            onDelete = ForeignKey.CASCADE // Comportamiento de eliminación (opcional)
        )
    ]
)
data class Cita(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: String,
    val hora: String,
    val descripcion: String,
    val userCedula: String, // Esta es la clave foránea
    var estado: String = "Pendiente",
    var descripcionEstado: String = ""
)