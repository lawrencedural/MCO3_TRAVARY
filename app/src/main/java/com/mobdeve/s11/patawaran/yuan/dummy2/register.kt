package com.mobdeve.s11.patawaran.yuan.dummy2

import android.content.Intent
import android.widget.ImageButton
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class register : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val editTextUsernameRegister: EditText = findViewById(R.id.editTextUsernameRegister)
        val editTextEmail: EditText = findViewById(R.id.editTextEmail)
        val editTextPasswordRegister: EditText = findViewById(R.id.editTextPasswordRegister)
        val editTextRePassword: EditText = findViewById(R.id.editTextRePassword)
        val buttonRegisterNow: Button = findViewById(R.id.buttonRegisterNow)

        buttonRegisterNow.setOnClickListener {
            val username = editTextUsernameRegister.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPasswordRegister.text.toString()
            val rePassword = editTextRePassword.text.toString()

            // Basic validation
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && rePassword.isNotEmpty() && password == rePassword) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Registration successful
                            Toast.makeText(baseContext, "Registration successful!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, Login::class.java) // Replace with your desired activity (Login in this case)
                            startActivity(intent)
                            finish()
                        } else {
                            // Registration failed
                            Toast.makeText(baseContext, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(baseContext, "Please fill in all fields and ensure passwords match.", Toast.LENGTH_SHORT).show()
            }
        }

        val backButton: ImageButton = findViewById(R.id.imageButton2)
        backButton.setOnClickListener {
            finish()
        }
    }
}
