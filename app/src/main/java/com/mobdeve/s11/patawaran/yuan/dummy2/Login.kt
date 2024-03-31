package com.mobdeve.s11.patawaran.yuan.dummy2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextUsername: EditText = findViewById(R.id.editTextUsername)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val checkBoxRememberMe: CheckBox = findViewById(R.id.checkBoxRememberMe)
        val buttonLogin: Button = findViewById(R.id.buttonLogin)

        // Check if user credentials should be remembered
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val rememberMeChecked = sharedPreferences.getBoolean("REMEMBER_ME", false)
        if (rememberMeChecked) {
            // If "Remember Me" is checked, populate the fields with saved credentials
            val savedUsername = sharedPreferences.getString("SAVED_USERNAME", "")
            val savedPassword = sharedPreferences.getString("SAVED_PASSWORD", "")
            editTextUsername.setText(savedUsername)
            editTextPassword.setText(savedPassword)
            checkBoxRememberMe.isChecked = true
        }

        // Set OnClickListener for the login button
        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            // Basic validation
            if (username.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Login successful
                            val intent = Intent(this, home::class.java)
                            startActivity(intent)

                            // Save user email and credentials to SharedPreferences if "Remember Me" is checked
                            if (checkBoxRememberMe.isChecked) {
                                saveUserCredentials(username, password)
                            }

                            finish()
                        } else {
                            // Login failed
                            Toast.makeText(baseContext, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(baseContext, "Please enter both username and password.", Toast.LENGTH_SHORT).show()
            }
        }

        // Set OnClickListener for the "Don't have an account? Register now." text
        val textViewRegisterNow: TextView = findViewById(R.id.textViewRegisterNow)
        textViewRegisterNow.setOnClickListener {
            val intent = Intent(this, register::class.java)
            startActivity(intent)
        }
    }

    private fun saveUserCredentials(username: String, password: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("REMEMBER_ME", true)
            putString("SAVED_USERNAME", username)
            putString("SAVED_PASSWORD", password)
            putString("USER_EMAIL", username)
            apply()
        }
    }
}
