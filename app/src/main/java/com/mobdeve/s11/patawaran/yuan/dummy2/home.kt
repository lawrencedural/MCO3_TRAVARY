package com.mobdeve.s11.patawaran.yuan.dummy2

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import profileMe

class home : AppCompatActivity() {

    private lateinit var btnHome: ImageButton
    private lateinit var btnMe: ImageButton
    private lateinit var btnRecord: ImageButton
    private lateinit var btnMaps: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnHome = findViewById(R.id.btnHome)
        btnMe = findViewById(R.id.btnMe)
        btnRecord = findViewById(R.id.btnRecord)
        btnMaps = findViewById(R.id.btnMaps)

        // After logging in, navigate to the homePage fragment
        val fragment = homePage()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun onHomeButtonClick(view: View) {
        resetButtonIcons()
        btnHome.setImageResource(R.drawable.finalhomespng)

        val fragment = homePage()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun onMapsButtonClick(view: View) {
        resetButtonIcons()
        btnMaps.setImageResource(R.drawable.finalmapsg)

        val fragment = mapsPage()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun onRecordButtonClick(view: View) {
        resetButtonIcons()
        btnRecord.setImageResource((R.drawable.finalrecordbgpng))

        val fragment = post()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    fun onMeButtonClick(view: View) {
        resetButtonIcons()
        btnMe.setImageResource(R.drawable.finalprofiles)

        val fragment = profileMe()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun resetButtonIcons() {
        btnHome.setImageResource(R.drawable.home)
        btnMe.setImageResource(R.drawable.user)
        btnRecord.setImageResource(R.drawable.record)
        btnMaps.setImageResource(R.drawable.map)
    }
}
