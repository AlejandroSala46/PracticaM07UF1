package com.example.practicam07uf1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.practicam07uf1.clases.DatabaseInstance
import com.example.practicam07uf1.clases.RetrofitClient
import com.example.practicam07uf1.clases.RocketEntity
import kotlinx.coroutines.launch

//Clase para mostrar la lista de cohetes
class RocketListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        // Llamada a la API para obtener los datos de los cohetes
        RetrofitClient.instance.getRockets().enqueue(object : Callback<List<RocketEntity>> {
            override fun onResponse(call: Call<List<RocketEntity>>, response: Response<List<RocketEntity>>) {
                // Si la respuesta es exitosa, guarda los datos en la base de datos
                if (response.isSuccessful) {
                    Log.d("Rocket", "Response received" + response.body())
                    val rockets = response.body()
                    rockets?.let {
                        saveRocketsToDatabase(it)
                        goToLogin() // Redirigir al login después de guardar los datos
                    }
                } else {
                    Toast.makeText(
                        this@RocketListActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    closeApp()
                }
            }

            override fun onFailure(call: Call<List<RocketEntity>>, t: Throwable) {
                // Si la petición falla, muestra un mensaje de error
                Toast.makeText(this@RocketListActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("Rocket", "Error de red: ${t.message}")
                closeApp()
            }
        })
    }

    // Redirige a la actividad de login
    private fun goToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    // Cierra la actividad
    private fun closeApp() {
        // Crear un Handler para retrasar el cierre de la actividad
        Handler(Looper.getMainLooper()).postDelayed({
            finish()  // Cierra la actividad
        }, 1000)  // 1000 milisegundos = 1 segundo
    }

    // Guarda los datos de los cohetes en la base de datos
    private fun saveRocketsToDatabase(rockets: List<RocketEntity>) {
        val rocketEntities = rockets.map { rocket ->
            RocketEntity(
                name = rocket.name,
                type = rocket.type,
                active = rocket.active,
                cost_per_launch = rocket.cost_per_launch,
                success_rate_pct = rocket.success_rate_pct,
                country = rocket.country,
                company = rocket.company,
                wikipedia = rocket.wikipedia,
                description = rocket.description,
                height = RocketEntity.Dim(
                    feet = rocket.height.feet,
                    meters = rocket.height.meters
                ),
                diameter = RocketEntity.Dim(
                    feet = rocket.diameter.feet,
                    meters = rocket.diameter.meters
                )


            )
        }

        // Usa lifecycleScope para ejecutar una corrutina
        lifecycleScope.launch {
            val db = DatabaseInstance.getInstance(this@RocketListActivity)


            try {
                db.rocketDao().insertAll(rocketEntities) // Inserta los datos
                Log.d("Database", "Datos insertados correctamente")
            } catch (e: Exception) {
                Log.e("Database", "Error al insertar los datos: ${e.message}")
            }
        }
    }

}
