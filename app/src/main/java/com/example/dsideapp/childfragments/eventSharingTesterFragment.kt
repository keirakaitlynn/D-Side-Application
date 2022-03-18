package com.example.dsideapp.childfragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.data.ActivityObject
import com.example.dsideapp.data.LocationObject
import com.google.firebase.database.FirebaseDatabase

class eventSharingTesterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v =  inflater.inflate(R.layout.fragment_event_sharing_tester, container, false)

        //Creating db references
        var authorization = auth
        var user = authorization.currentUser
        var userId = authorization.currentUser?.uid
        var db = FirebaseDatabase.getInstance().getReference()

        //Getting the friend's UID
        var friendId = "ykjbl1dDd5fyvr0g5WqqUNlmHWM2"
        //var friendId = db.child("users").child(userId.toString()).child("data").child("friends").child("IaV5KRr1XEXqvVHENqI4ukP4qVm2")
        //Creating writeToDB function
        db.child("users").child(friendId).child("data").child("events")
            .child("WHBqJbAom0Yz0MQPQg0zuDnv4Xv1").setValue("Testing")

        Log.w("User ID: ", userId.toString())
        Log.w("Current User: ", user.toString())

        return v
    }
}