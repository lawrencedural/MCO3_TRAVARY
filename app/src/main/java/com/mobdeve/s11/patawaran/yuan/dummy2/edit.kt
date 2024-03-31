package com.mobdeve.s11.patawaran.yuan.dummy2

import android.widget.EditText
import android.widget.Button
import android.content.Intent
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class edit : AppCompatActivity() {

    private lateinit var editTextNewName: EditText
    private lateinit var editTextNewBio: EditText
    private lateinit var buttonSaveChanges: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        editTextNewName = findViewById(R.id.editName)
        editTextNewBio = findViewById(R.id.editBio)
        buttonSaveChanges = findViewById(R.id.btnSave)
    }

    fun onBackButtonClick(view: View) {
        finish() // Close the current activity to go back
    }

    fun onSaveButtonClick(view: View) {
        val newName = editTextNewName.text.toString()
        val newBio = editTextNewBio.text.toString()

        val resultIntent = Intent()
        resultIntent.putExtra("NEW_NAME", newName)
        resultIntent.putExtra("NEW_BIO", newBio)
        setResult(Activity.RESULT_OK, resultIntent)

        finish() // Close the current activity
    }
}
