package com.example.practicam07uf1.clases

import android.content.Context
import androidx.room.Room

//Creamos la instancia de la base de datos
object DatabaseInstance {
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "rockets_database"
            ).build()
        }
        return instance!!
    }
}
