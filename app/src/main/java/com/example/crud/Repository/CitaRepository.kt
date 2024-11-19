package com.example.crud.Repository

import com.example.crud.DAO.CitaDao
import com.example.crud.Model.Cita
import com.example.crud.Model.User

class CitaRepository(private val citaDao: CitaDao) {

    suspend fun insert(cita: Cita) {
        citaDao.insert(cita)
    }

    suspend fun getCitasByCedula(cedula: String): List<Cita> {
        return citaDao.getCitasByCedula(cedula)
    }
    suspend fun deleteCita(cita: Cita) {
        citaDao.deleteCita(cita)
    }
    suspend fun getAllCitas(): List<Cita> = citaDao.getAllCitas()

    suspend fun actualizarEstadoCita(citaId: Int, nuevoEstado: String, descripcion: String) {
        citaDao.actualizarEstadoCita(citaId, nuevoEstado, descripcion)
    }
    suspend fun getCitasByFecha(fecha: String): List<Cita> {
        return citaDao.getCitasByFecha(fecha)

    }

    suspend fun getCitasCountByUser(userCedula: String): Int {
        return citaDao.getCitasCountByUser(userCedula)
    }

    suspend fun getUserCitaCountList(users: List<User>): List<Pair<User, Int>> {
        return users.map { user ->
            user to getCitasCountByUser(user.cedula)
        }
    }
    suspend fun getAllCitasCount(): Int {
        return citaDao.getAllCitas().size
    }



}