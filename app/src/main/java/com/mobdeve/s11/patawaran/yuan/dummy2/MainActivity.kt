package com.mobdeve.s11.patawaran.yuan.dummy2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import android.view.ViewTreeObserver

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Reference to MotionLayout
        val motionLayout: MotionLayout = findViewById(R.id.motionLayout)

        // Delay the transition until the layout is completely drawn
        motionLayout.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                // Remove the listener to avoid multiple calls
                motionLayout.viewTreeObserver.removeOnPreDrawListener(this)

                // Start the transition
                motionLayout.transitionToEnd()
                return true
            }
        })

        // Set a listener to detect when the transition completes
        motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                // Not needed, but required to implement all methods of the interface
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                // Not needed, but required to implement all methods of the interface
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                // Transition completed, navigate to the login activity
                navigateToLoginActivity()
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                // Not needed, but required to implement all methods of the interface
            }
        })
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish() // Finish the current activity to prevent going back to it when pressing back from the login activity
    }
}
