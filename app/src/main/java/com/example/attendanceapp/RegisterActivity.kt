package com.example.attendanceapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.attendanceapp.AuthModels.RegisterRequest
import com.example.attendanceapp.network.ApiClient
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    // Removed @SuppressLint("WrongViewCast") as it's not needed with correct setup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() is commented out as it's not standard and might not be defined in your project.
        // If you have a specific library for it, you can re-enable it.
        // enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // get data from register
        val name = findViewById<TextView>(R.id.name)
        val email = findViewById<TextView>(R.id.email)
        val password = findViewById<TextView>(R.id.password)
        val btnRegister = findViewById<MaterialButton>(R.id.btnRegister)
        val gotoLogin = findViewById<MaterialButton>(R.id.goto_login)

        btnRegister.setOnClickListener {
            val inputName = name.text.toString().trim()
            val inputEmail = email.text.toString().trim()
            val inputPassword = password.text.toString().trim()

            if (inputName.isEmpty() || inputEmail.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnRegister.isEnabled = false
            btnRegister.text = "Registering..."

            val apiService = ApiClient.apiService
            val registerRequest = RegisterRequest(inputName, inputEmail, inputPassword)

            // Launch a coroutine in the lifecycle scope
            lifecycleScope.launch {
                try {
                    // Call the suspend function directly from the coroutine
                    val response = apiService.register(registerRequest)

                    btnRegister.isEnabled = true
                    btnRegister.text = "Register"

                    if (response.isSuccessful) {
                        val body = response.body()
                        Toast.makeText(this@RegisterActivity, body?.message ?: "Registered successfully", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errMsg = response.errorBody()?.string()
                        Toast.makeText(this@RegisterActivity, "Registration failed: ${errMsg ?: "Unknown error"}", Toast.LENGTH_LONG).show()
                    }
                } catch (e: IOException) {
                    // Handle network errors (e.g., no internet)
                    btnRegister.isEnabled = true
                    btnRegister.text = "Register"
                    Toast.makeText(this@RegisterActivity, "Network error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    // Handle other unexpected errors
                    btnRegister.isEnabled = true
                    btnRegister.text = "Register"
                    Toast.makeText(this@RegisterActivity, "An unexpected error occurred: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }

        // intent to login
        gotoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
