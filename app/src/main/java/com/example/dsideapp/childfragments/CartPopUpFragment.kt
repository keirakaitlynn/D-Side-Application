package com.example.dsideapp.childfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.dsideapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.example.dsideapp.LoginActivity
import com.google.firebase.database.DataSnapshot

class CartPopUpFragment : Fragment() {
    var authGlobal = LoginActivity()
    private var auth = authGlobal.auth
    private lateinit var popUpText: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_cart_pop_up, container, false)
        /*//In here is where we get the info from database to populate the popup window
        val user = auth.currentUser
        if (user != null) {
            println("WORK")
            print(user.uid)
        }
        else{
            print("PAIN")
        }
        val activityInfo = user?.let { FirebaseDatabase.getInstance().getReference(it.uid) }
        */
        return v
    }
}