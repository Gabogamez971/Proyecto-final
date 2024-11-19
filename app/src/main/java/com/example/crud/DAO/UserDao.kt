package com.example.crud.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.crud.Model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrentUser(): User?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users")
    suspend fun getUser(): User?

    @Query("DELETE FROM users WHERE cedula = :userCedula")
    suspend fun delete(userCedula: String)

    @Query("UPDATE users SET nombre = :nombre, apellido = :apellido, edad = :edad, peso = :peso, altura = :altura, contraseña = :contraseña WHERE cedula = :userCedula")
    suspend fun update(userCedula: String, nombre: String, apellido: String, edad: Int, peso: Double, altura: Double, contraseña: String)

    @Query("SELECT * FROM users WHERE cedula = :userCedula")
    suspend fun getUserByCedula(userCedula: String): User?

    // Nueva consulta para verificar si ya existe un usuario con la cédula dada
    @Query("SELECT COUNT(*) FROM users WHERE cedula = :userCedula")
    suspend fun exists(userCedula: String): Int

    @Query("SELECT * FROM users WHERE cedula = :userCedula AND contraseña = :password")
    suspend fun getUserByCedulaAndPassword(userCedula: String, password: String): User?

    // Método para obtener el usuario administrador si fuera necesario
    @Query("SELECT * FROM users WHERE cedula = 'admin123'")
    suspend fun getAdminUser(): User?




}