import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mobdeve.s11.patawaran.yuan.dummy2.R
import com.mobdeve.s11.patawaran.yuan.dummy2.edit

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class profileMe : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userEmail: String // Declare userEmail here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Handle arguments if needed
        }

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userEmail = sharedPreferences.getString("USER_EMAIL", "") ?: ""

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle Edit button click
        view.findViewById<Button>(R.id.btnEdit).setOnClickListener {
            val intent = Intent(activity, edit::class.java)
            startActivityForResult(intent, EDIT_REQUEST_CODE)
        }

        // Load saved data from SharedPreferences and update UI
        val savedName = sharedPreferences.getString("${userEmail}_SAVED_NAME", "Your Name")
        val savedBio = sharedPreferences.getString("${userEmail}_SAVED_BIO", "Your Bio")
        updateUI(savedName, savedBio)

        // Display user email at the top
        view.findViewById<TextView>(R.id.userEmail).text = userEmail
    }

    // Handle the result from the edit activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Update the UI with the edited data
            val newName = data?.getStringExtra("NEW_NAME")
            val newBio = data?.getStringExtra("NEW_BIO")

            // Save edited data to SharedPreferences
            with(sharedPreferences.edit()) {
                putString("${userEmail}_SAVED_NAME", newName)
                putString("${userEmail}_SAVED_BIO", newBio)
                apply()
            }

            updateUI(newName, newBio)
        }
    }

    private fun updateUI(newName: String?, newBio: String?) {
        // Update UI elements with the edited or saved data
        view?.findViewById<TextView>(R.id.profileName)?.text = newName
        view?.findViewById<TextView>(R.id.profileBio)?.text = newBio
    }

    companion object {
        private const val EDIT_REQUEST_CODE = 1

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            profileMe().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
