package com.mobdeve.s11.patawaran.yuan.dummy2

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class record : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomePageAdapter

    companion object {
        private const val PERMISSION_REQUEST_CAMERA = 100 // Define permission request code
        private const val REQUEST_IMAGE_CAPTURE = 101 // Define image capture request code
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        dbHelper = DatabaseHelper(this)

        // Find the RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with an empty list initially
        adapter = HomePageAdapter(emptyList())
        recyclerView.adapter = adapter

        // Find the capture button
        val captureButton = findViewById<Button>(R.id.captureButton)

        // Set OnClickListener for the capture button
        captureButton.setOnClickListener {
            // Check camera permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is not granted, request the permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        PERMISSION_REQUEST_CAMERA
                    )
                } else {
                    // Permission is already granted, launch the camera
                    dispatchTakePictureIntent()
                }
            }
        }

        // Find the ImageButton for back
        val backButton = findViewById<ImageButton>(R.id.imageButton2)

        // Set OnClickListener for the back button
        backButton.setOnClickListener {
            // Finish the activity when the back button is clicked
            finish()
        }

        // Find the Post button
        val postButton = findViewById<Button>(R.id.buttonPost)

        postButton.setOnClickListener {
            // Get the values of caption and location EditText fields
            val caption = findViewById<EditText>(R.id.editCaption).text.toString()
            val location = findViewById<EditText>(R.id.editLocation).text.toString()

            // Check if all required information is provided
            if (caption.isNotEmpty() && location.isNotEmpty()) {
                // If all required fields are filled, proceed with posting
                // Insert the post into the database
                insertPost(caption, location)
            } else {
                // If any required field is empty, display a toast message
                Toast.makeText(
                    this,
                    "Please provide all required information",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Function to launch the camera
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    // Check permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now launch the camera
                dispatchTakePictureIntent()
            } else {
                // Permission denied, show a message or handle the denial case
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertPost(caption: String, location: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.PostEntry.COLUMN_CAPTION, caption)
            put(DatabaseContract.PostEntry.COLUMN_LOCATION, location)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DatabaseContract.PostEntry.TABLE_NAME, null, values)

        // Display a toast message indicating the result of the insertion
        if (newRowId != -1L) {
            Toast.makeText(this, "Post inserted successfully", Toast.LENGTH_SHORT).show()

            // Refresh the posts displayed in the homePage fragment after posting
            val homePageFragment = supportFragmentManager.findFragmentByTag("homePage")
            if (homePageFragment != null && homePageFragment is homePage) {
                homePageFragment.refreshPosts()
            }

            // Finish the current activity
            finish()
        } else {
            Toast.makeText(this, "Failed to insert post", Toast.LENGTH_SHORT).show()
        }
    }
}
