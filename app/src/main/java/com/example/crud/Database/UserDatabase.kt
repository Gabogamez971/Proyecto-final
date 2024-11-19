package com.example.crud.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.crud.DAO.UserDao
import com.example.crud.Model.User


import com.example.crud.DAO.CitaDao
import com.example.crud.Model.Cita
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [User::class, Cita::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun citaDao(): CitaDao // Asegúrate de que el DAO para citas esté aquí

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database" // Nombre de la base de datos
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    // Llama a la función para agregar el administrador predeterminado
                    database.prepopulateDatabase()
                }
            }
        }
    }
    suspend fun prepopulateDatabase() {
        val userDao = userDao()
        // Verifica si ya existe un administrador
        if (userDao.getUserByCedula("admin") == null) {
            // Crea un administrador predeterminado
            val adminUser = User(
                cedula = "admin",
                nombre = "Administrador",
                apellido = "Predeterminado",
                edad = 30,
                peso = 70.0,
                altura = 1.75,
                contraseña = "admin123",
                isAdmin = true
            )
            userDao.insert(adminUser)
        }
    }

}
