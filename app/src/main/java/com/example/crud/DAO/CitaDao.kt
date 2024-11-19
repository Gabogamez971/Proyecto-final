package com.example.crud.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.crud.Model.Cita
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cita: Cita)

    @Query("SELECT * FROM citas WHERE userCedula = :cedula")
    suspend fun getCitasByCedula(cedula: String): List<Cita>

    @Delete
    suspend fun deleteCita(cita: Cita)

    @Query("SELECT * FROM citas")
    suspend fun getAllCitas(): List<Cita>

    @Query("UPDATE citas SET estado = :nuevoEstado, descripcion = :descripcion WHERE id = :citaId")
    suspend fun actualizarEstadoCita(citaId: Int, nuevoEstado: String, descripcion: String)

    @Query("SELECT * FROM citas WHERE fecha = :fecha")
    suspend fun getCitasByFecha(fecha: String): List<Cita>

    @Query("SELECT COUNT(*) FROM citas WHERE userCedula = :userCedula")
    suspend fun getCitasCountByUser(userCedula: String): Int

    @Query("SELECT COUNT(*) FROM citas")
    suspend fun getAllCitasCount(): Int




}