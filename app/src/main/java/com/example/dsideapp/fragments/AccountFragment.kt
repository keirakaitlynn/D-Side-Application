package com.example.dsideapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dsideapp.LoginActivity
import com.example.dsideapp.R
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    lateinit var button: Button
    lateinit var textView: TextView
    private lateinit var viewOfLayout: View
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout =
        inflater.inflate(R.layout.fragment_account, container, false)

        FirebaseAuth.getInstance()

        button = viewOfLayout.findViewById<View>(R.id.log_out) as Button
        //textView = findViewById(R.id.logged_in)

        //textView.setText("Welcome")

        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            //Log.w("USER ID ", auth.uid.toString())
            val intent = Intent (activity, LoginActivity::class.java)
            activity?.startActivity(intent)
            //finish()
            //viewOfLayout = inflater.inflate(R.layout.activity_login, container, false)
            //startActivity(intent)

        }

        return viewOfLayout
    }

}