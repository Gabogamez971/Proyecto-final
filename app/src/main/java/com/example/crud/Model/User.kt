package com.example.crud.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val cedula: String,
    val nombre: String,
    val apellido: String,
    val edad: Int = 0,
    val peso: Double = 0.0,
    val altura: Double = 0.0,
    val contraseña: String,
    val isAdmin: Boolean = false
)
