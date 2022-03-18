package com.example.dsideapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.dsideapp.LoginActivity
import com.example.dsideapp.R
import com.google.firebase.auth.FirebaseAuth
import com.example.dsideapp.auth
import com.google.firebase.database.FirebaseDatabase

class AccountFragment : Fragment() {
    lateinit var imageButton: ImageButton
    lateinit var textView: TextView
    private lateinit var viewOfLayout: View
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //TRY RUNNING AND SEE WHAT HAPPEND <------
        //DB info//
        var authorization = com.example.dsideapp.auth
        var user = authorization.currentUser
        var userID = authorization.currentUser?.uid
        var db = FirebaseDatabase.getInstance().getReference()

        viewOfLayout =
            inflater.inflate(R.layout.fragment_account, container, false)

        FirebaseAuth.getInstance()

        imageButton = viewOfLayout.findViewById<View>(R.id.log_out) as ImageButton
        //textView = findViewById(R.id.logged_in)

        //textView.setText("Welcome")

        imageButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            //Log.w("USER ID ", auth.uid.toString())
            val intent = Intent(activity, LoginActivity::class.java)
            activity?.startActivity(intent)
            //finish()
            //viewOfLayout = inflater.inflate(R.layout.activity_login, container, false)
            //startActivity(intent)

        }

        var saveChangesButton = viewOfLayout.findViewById<Button>(R.id.changesSaveButton)
        saveChangesButton.setOnClickListener{
            db.child("users").child(userID.toString()).child("username")
                    .setValue(viewOfLayout.findViewById<EditText>(R.id.user_name).text.toString())
        }

        return viewOfLayout
    }
}