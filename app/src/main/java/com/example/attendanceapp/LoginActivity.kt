package com.example.attendanceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.attendanceapp.R
import com.example.attendanceapp.ViewModel.LoginState
import com.example.attendanceapp.ViewModel.LoginViewModel
import com.example.attendanceapp.AuthModels.User


class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // create a layout with ids below

        val emailInput = findViewById<EditText>(R.id.email)
        val passwordInput = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val statusText = findViewById<TextView>(R.id.sms_show)
        val progress = findViewById<ProgressBar>(R.id.progressBar)

        loginButton.setOnClickListener {
            val e = emailInput.text.toString().trim()
            val p = passwordInput.text.toString()
            viewModel.login(e, p)
        }

        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is LoginState.Idle -> {
                        progress.visibility = ProgressBar.INVISIBLE
                        statusText.text = ""
                    }
                    is LoginState.Loading -> {
                        progress.visibility = ProgressBar.VISIBLE
                        statusText.text = "Logging in..."
                    }
                    is LoginState.Success -> {
                        progress.visibility = ProgressBar.INVISIBLE
                        statusText.text = "Logged in successfully"
                        val user = state.user.user
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                    is LoginState.Error -> {
                        progress.visibility = ProgressBar.INVISIBLE
                        statusText.text = state.message
                    }
                }
            }
        }
    }
}