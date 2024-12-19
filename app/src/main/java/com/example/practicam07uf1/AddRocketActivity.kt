package com.example.practicam07uf1

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.practicam07uf1.clases.AppDatabase
import com.example.practicam07uf1.clases.RocketEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

//Clase para añadir cohetes
class AddRocketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addrocket)

        //Cargamos los datos de la vista
        val editName = findViewById<EditText>(R.id.edit_name)
        val editType = findViewById<EditText>(R.id.edit_type)
        val checkActive = findViewById<CheckBox>(R.id.check_active)
        val editCostPerLaunch = findViewById<EditText>(R.id.edit_cost_per_launch)
        val editSuccessRate = findViewById<EditText>(R.id.edit_success_rate)
        val editCountry = findViewById<EditText>(R.id.edit_country)
        val editCompany = findViewById<EditText>(R.id.edit_company)
        val editWikipedia = findViewById<EditText>(R.id.edit_wikipedia)
        val editDescription = findViewById<EditText>(R.id.edit_description)
        val editHeightMeters = findViewById<EditText>(R.id.edit_height_meters)
        val editDiameterMeters = findViewById<EditText>(R.id.edit_diameter_meters)
        val btnAddRocket = findViewById<Button>(R.id.btn_add_rocket)

        // Acción del botón "Añadir cohete"
        btnAddRocket.setOnClickListener {
            //Obtenemos los datos
            val name = editName.text.toString()
            val type = editType.text.toString()
            val active = checkActive.isChecked
            val costPerLaunch = editCostPerLaunch.text.toString().toDoubleOrNull() ?: 0.0
            val successRate = editSuccessRate.text.toString().toDoubleOrNull() ?: 0.0
            val country = editCountry.text.toString()
            val company = editCompany.text.toString()
            val wikipedia = editWikipedia.text.toString()
            val description = editDescription.text.toString()
            val heightMeters = editHeightMeters.text.toString().toDoubleOrNull() ?: 0.0
            val diameterMeters = editDiameterMeters.text.toString().toDoubleOrNull() ?: 0.0

            // Validación de datos obligatorios
            if (name.isBlank() || type.isBlank() || country.isBlank() || company.isBlank() || description.isBlank() || heightMeters <= 0.0 || diameterMeters <= 0.0) {
                Toast.makeText(this, "Por favor rellena todos los datos obligatorios y valida la información.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Convertimos las medidas a pies
            var diamFts = diameterMeters.toDouble() * 3.28084;
            var heiFts = heightMeters.toDouble() * 3.28084;

            // Crear el objeto RocketEntity
            val rocket = RocketEntity(
                name = name,
                type = type,
                active = active,
                cost_per_launch = costPerLaunch,
                success_rate_pct = successRate,
                country = country,
                company = company,
                wikipedia = wikipedia,
                description = description,
                height = RocketEntity.Dim(heightMeters, heiFts),
                diameter = RocketEntity.Dim(diameterMeters, diamFts)
            )

            // Insertar en la base de datos usando una corrutina
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getInstance(this@AddRocketActivity)
                db.rocketDao().insertRocket(rocket)

                // Volver al hilo principal para mostrar el mensaje de éxito
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddRocketActivity, "Rocket added successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Cierra esta actividad
                }
            }
        }
    }
}