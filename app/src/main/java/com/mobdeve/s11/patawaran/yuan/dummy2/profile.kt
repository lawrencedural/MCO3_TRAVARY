package com.mobdeve.s11.patawaran.yuan.dummy2

import android.widget.TextView
import android.content.Intent
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class profile : AppCompatActivity() {
    private val EDIT_REQUEST_CODE = 123 // Any unique request code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Retrieve the saved data from SharedPreferences
        val sharedPreferences = getSharedPreferences("profile_data", MODE_PRIVATE)
        val defaultName = intent.getStringExtra("DEFAULT_NAME") ?: "Default Name"
        val name = sharedPreferences.getString("name", defaultName)
        val bio = sharedPreferences.getString("bio", "Default Bio")

        // Initialize profile UI with the retrieved data
        val profileName = findViewById<TextView>(R.id.profileName)
        profileName.text = name

        val profileBio = findViewById<TextView>(R.id.profileBio)
        profileBio.text = bio
    }

    fun onEditButtonClick(view: View) {
        val intent = Intent(this, edit::class.java)
        startActivityForResult(intent, EDIT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val newName = data?.getStringExtra("NEW_NAME")
            val newBio = data?.getStringExtra("NEW_BIO")

            // Save the edited data to SharedPreferences
            saveProfileData(newName, newBio)

            // Update your profile UI with the new data
            val profileName = findViewById<TextView>(R.id.profileName)
            val profileBio = findViewById<TextView>(R.id.profileBio)

            newName?.let { profileName.text = it }
            newBio?.let { profileBio.text = it }
        }
    }

    private fun saveProfileData(name: String?, bio: String?) {
        val sharedPreferences = getSharedPreferences("profile_data", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("name", name)
        editor.putString("bio", bio)

        editor.apply()
    }
}
