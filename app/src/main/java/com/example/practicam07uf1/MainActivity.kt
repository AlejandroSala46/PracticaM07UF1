package com.example.practicam07uf1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practicam07uf1.clases.AppDatabase
import com.example.practicam07uf1.clases.RocketEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Clase para la vista principal
class MainActivity : AppCompatActivity() {

    private lateinit var rocketListContainer: LinearLayout
    private lateinit var searchBar: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmenu)

        // Referencias a los elementos de la vista
        rocketListContainer = findViewById(R.id.rocketListContainer)
        searchBar = findViewById(R.id.searchBar)
        addButton = findViewById(R.id.btnAdd)

        // Cargar y mostrar la lista de cohetes
        loadRocketList("")

        //Barra de busqueda
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                loadRocketList(s.toString())
            }
        })

        // Acción del botón "Añadir"
        addButton.setOnClickListener {
            // Navegar a la actividad de añadir cohete
            val intent = Intent(this@MainActivity, AddRocketActivity::class.java)
            startActivity(intent)
            loadRocketList("")
        }


    }

    // Carga y muestra la lista de cohetes
    private fun loadRocketList(filter: String) {
        // Cargar la lista de cohetes de la base de datos
        GlobalScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getInstance(applicationContext)
            val rockets = if (filter.isEmpty()) {
                db.rocketDao().getAllRockets()
            } else {
                db.rocketDao().searchByName("%$filter%")
            }

            // Mostrar la lista de cohetes en la vista
            withContext(Dispatchers.Main) {
                rocketListContainer.removeAllViews()

                rockets.forEach { rocket ->
                    val button = Button(this@MainActivity)
                    button.text = rocket.name
                    button.setOnClickListener {
                        // Navegar a la actividad de detalles del cohete
                        val intent = Intent(this@MainActivity, RocketDetailActivity::class.java)
                        intent.putExtra("rocket", rocket)
                        startActivity(intent)


                        // Mostrar un mensaje de selección
                        Toast.makeText(this@MainActivity, "Seleccionado: ${rocket.name}", Toast.LENGTH_SHORT).show()
                    }
                    rocketListContainer.addView(button)
                }
            }
        }
    }
}
