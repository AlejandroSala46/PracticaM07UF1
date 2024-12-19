package com.example.practicam07uf1.clases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

//Interfaz para acceder a la base de datos
@Dao
interface RocketDao {

    // Inserta los cohetes en la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rockets: List<RocketEntity>)

    // Inserta un solo cohete
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRocket(rocket: RocketEntity)

    // Recupera todos los cohetes
    @Query("SELECT * FROM rockets")
    suspend fun getAllRockets(): List<RocketEntity>

    // Recupera cohetes por nombre
    @Query("SELECT * FROM rockets WHERE name LIKE :name")
    suspend fun searchByName(name: String): List<RocketEntity>

    @Update
    suspend fun updateRocket(rocket: RocketEntity)

    @Delete
    suspend fun deleteRocket(rocket: RocketEntity)


}
