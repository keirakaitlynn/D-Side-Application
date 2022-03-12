package com.example.dsideapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.google.firebase.database.FirebaseDatabase

class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //DB info
        var authorization = auth
        var user = authorization.currentUser
        var userID = authorization.currentUser?.uid
        var db = FirebaseDatabase.getInstance().getReference()

        var v = inflater.inflate(R.layout.fragment_account, container, false)
        var saveChangesButton = v.findViewById<Button>(R.id.changesSaveButton)
        saveChangesButton.setOnClickListener{
            db.child("users").child(userID.toString()).child("username")
                .setValue(v.findViewById<EditText>(R.id.user_name).text.toString())
        }
        return v
    }
}