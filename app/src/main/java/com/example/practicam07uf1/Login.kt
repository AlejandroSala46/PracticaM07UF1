package com.example.practicam07uf1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

//Clase para el login
class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Referencias a los campos y botón
        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        // Credenciales correctas
        val correctUsername = "admin"
        val correctPassword = "1234"


        btnLogin.setOnClickListener {
            // Obtener el nombre de usuario y la contraseña ingresados
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validar las credenciales
            if (username == correctUsername && password == correctPassword) {
                // Login exitoso
                Toast.makeText(this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Credenciales incorrectas
                Toast.makeText(this, "Usuario o contraseña incorrectos. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
                etPassword.text.clear()
            }
        }
    }
}
