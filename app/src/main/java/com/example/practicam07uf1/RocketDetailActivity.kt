package com.example.practicam07uf1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicam07uf1.ui.theme.PracticaM07UF1Theme
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practicam07uf1.clases.RocketEntity
import com.example.practicam07uf1.clases.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Clase para mostrar los detalles del cohete
class RocketDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rocketdetail)

        // Recuperar el cohete del Intent
        val rocket = intent.getParcelableExtra<RocketEntity>("rocket")

        setupTable(rocket!!)

        val btnEdit = findViewById<Button>(R.id.btn_edit)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val btnDelete = findViewById<Button>(R.id.btn_delete)

        val tableLayout = findViewById<TableLayout>(R.id.details_table)

        // Botón Editar
        btnEdit.setOnClickListener {
            enableEditing(true, tableLayout)
            btnSave.visibility = View.VISIBLE
            btnEdit.visibility = View.GONE
        }

        // Botón Guardar
        btnSave.setOnClickListener {
            if (rocket != null) {
                saveChanges(tableLayout, rocket)
            }
            enableEditing(false, tableLayout)
            btnSave.visibility = View.GONE
            btnEdit.visibility = View.VISIBLE
        }

        // Botón Eliminar
        btnDelete.setOnClickListener {
            if (rocket != null) {
                deleteRocket(rocket)
            }
        }
    }

    private fun setupTable(rocket: RocketEntity) {
        val tableLayout = findViewById<TableLayout>(R.id.details_table)

        // Crear filas para cada detalle del cohete
        addRow(tableLayout, "Name", rocket.name)
        addRow(tableLayout, "Type", rocket.type)
        addRow(tableLayout, "Active", rocket.active.toString())
        addRow(tableLayout, "Cost Per Launch", rocket.cost_per_launch.toString())
        addRow(tableLayout, "Success Rate (%)", rocket.success_rate_pct.toString())
        addRow(tableLayout, "Country", rocket.country)
        addRow(tableLayout, "Company", rocket.company)
        addRow(tableLayout, "Wikipedia", rocket.wikipedia)
        addRow(tableLayout, "Description", rocket.description)
        addRow(tableLayout, "Height (m)", rocket.height.meters.toString()+"m")
        addRow(tableLayout, "Height (ft)", rocket.height.feet.toString()+"ft")
        addRow(tableLayout, "Diameter (m)", rocket.diameter.meters.toString()+"m")
        addRow(tableLayout, "Diameter (ft)", rocket.diameter.feet.toString()+"ft")
    }

    private fun addRow(tableLayout: TableLayout, key: String, value: String) {
        val row = TableRow(this)

        // TextView para la clave (el nombre del campo)
        val keyView = TextView(this).apply {
            text = key
            setPadding(8, 8, 8, 8)
            setTextColor(resources.getColor(android.R.color.white))
            setTypeface(null, android.graphics.Typeface.BOLD)
            layoutParams = TableRow.LayoutParams(
                0, // Peso flexible
                TableRow.LayoutParams.WRAP_CONTENT,
                1f // Asegura que ocupe una parte proporcional de la fila
            )
        }

        // EditText para el valor (para que se pueda editar el valor cuando sea necesario)
        val valueView = EditText(this).apply {
            setPadding(8, 8, 8, 8)
            setTextColor(resources.getColor(android.R.color.white))
            isSingleLine = false // Permitir múltiples líneas
            setText(value)
            setEnabled(false)
            layoutParams = TableRow.LayoutParams(
                0, // Peso flexible
                TableRow.LayoutParams.WRAP_CONTENT,
                1f // Asegura que ocupe una parte proporcional de la fila
            )

            // Si es un enlace, hazlo clickeable
            if (key == "Wikipedia") {
                setTextColor(resources.getColor(android.R.color.holo_blue_dark))
                setEnabled(true)
                isClickable = true
                paint.isUnderlineText = true // Subraya el texto
                setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))
                    startActivity(intent)
                }
            }
            else if (key == "Country") {
                val text : String = value
                setTextColor(resources.getColor(android.R.color.holo_blue_dark))
                setEnabled(true)
                isClickable = true
                paint.isUnderlineText = true // Subraya el texto
                setOnClickListener {
                    // Abrir Google Maps con el nombre del país
                    val geoUri =
                        Uri.parse("geo:0,0?q=$value") // Usa el nombre del país en la búsqueda
                    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                    mapIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(mapIntent)
                }
            }

        }

        // Asegúrate de que el TextView y EditText tengan una distribución adecuada
        row.addView(keyView)  // Añade el TextView para la clave
        row.addView(valueView)  // Añade el EditText para el valor

        // Agregar la fila a la tabla
        tableLayout.addView(row)
    }


    private fun enableEditing(enable: Boolean, tableLayout: TableLayout) {
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as TableRow
            val valueView = row.getChildAt(1) as EditText // Aquí ya es seguro que será EditText
            valueView.setEnabled(enable) // Habilitar o deshabilitar el EditText
        }
    }

    private fun saveChanges(tableLayout: TableLayout, rocket: RocketEntity) {
        // Aquí tomas los valores de los campos editados y actualizas el cohete
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as TableRow
            val keyView = row.getChildAt(0) as TextView
            val valueView = row.getChildAt(1) as EditText

            val updatedValue = valueView.text.toString()

            when (keyView.text.toString()) {
                "Name" -> rocket.name = updatedValue
                "Type" -> rocket.type = updatedValue
                "Active" -> rocket.active = updatedValue.toBoolean()
                "Cost Per Launch" -> rocket.cost_per_launch = updatedValue.toDoubleOrNull() ?: 0.0
                "Success Rate (%)" -> rocket.success_rate_pct = updatedValue.toDoubleOrNull() ?: 0.0
                "Country" -> rocket.country = updatedValue
                "Company" -> rocket.company = updatedValue
                "Wikipedia" -> rocket.wikipedia = updatedValue
                "Description" -> rocket.description = updatedValue
                "Height (m)" -> rocket.height.meters = updatedValue.toDoubleOrNull() ?: 0.0
                "Height (ft)" -> rocket.height.feet = updatedValue.toDoubleOrNull() ?: 0.0
                "Diameter (m)" -> rocket.diameter.meters = updatedValue.toDoubleOrNull() ?: 0.0
                "Diameter (ft)" -> rocket.diameter.feet = updatedValue.toDoubleOrNull() ?: 0.0
            }
        }

        // Guardar los cambios en la base de datos
        // Insertar en la base de datos usando una corrutina
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getInstance(this@RocketDetailActivity)
            db.rocketDao().updateRocket(rocket)

            // Volver al hilo principal para mostrar el mensaje de éxito
            withContext(Dispatchers.Main) {
                Toast.makeText(this@RocketDetailActivity, "Rocket updated successfully!", Toast.LENGTH_SHORT).show()
                finish() // Cierra esta actividad
            }
        }
    }

    private fun deleteRocket(rocket: RocketEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getInstance(this@RocketDetailActivity)
            db.rocketDao().deleteRocket(rocket)

            // Volver al hilo principal para mostrar el mensaje de éxito
            withContext(Dispatchers.Main) {
                Toast.makeText(this@RocketDetailActivity, "Rocket updated successfully!", Toast.LENGTH_SHORT).show()
                finish() // Cierra esta actividad
            }
        }
    }
}