package com.example.krud.Repository

import android.util.Log
import com.example.crud.DAO.UserDao
import com.example.crud.Model.User


class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: User): Boolean {
        // Verifica si ya existe un usuario con la misma cédula
        val exists = userDao.exists(user.cedula) > 0
        Log.d("UserRepository", "Usuario existe: $exists")

        return if (!exists) {
            userDao.insert(user)
            Log.d("UserRepository", "Usuario insertado: ${user.cedula}")
            true // Retorna 'true' si se insertó correctamente
        } else {
            Log.d("UserRepository", "No se insertó porque ya existe un usuario con la cédula: ${user.cedula}")
            false // Retorna 'false' si el usuario ya existe
        }
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    suspend fun delete(userCedula: String) {
        userDao.delete(userCedula)
    }

    suspend fun update(user: User) {
        userDao.update(user.cedula, user.nombre, user.apellido, user.edad, user.peso, user.altura, user.contraseña)
    }
    suspend fun exists(cedula: String): Boolean {
        return userDao.exists(cedula) > 0
    }
    suspend fun getUserByCedula(cedula: String): User? {
        return userDao.getUserByCedula(cedula)
    }

    suspend fun loginUser(cedula: String, password: String): User? {
        return userDao.getUserByCedulaAndPassword(cedula, password)
    }

    suspend fun getCurrentUser(): User? {
        return userDao.getCurrentUser()
    }
    suspend fun getAdminUser(): User? {
        return userDao.getAdminUser()

    }




}