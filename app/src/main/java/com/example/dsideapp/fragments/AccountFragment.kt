package com.example.dsideapp.fragments

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.data.FriendClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.example.dsideapp.childfragments.InformationChildFragment
import com.example.dsideapp.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AccountFragment : Fragment() {
    private lateinit var listView  : ListView
    lateinit var infoButton : ImageButton
    private val infoFragment = InformationChildFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //DB info//
        var authorization = auth
        var user = authorization.currentUser
        var userID = authorization.currentUser?.uid
        var db = FirebaseDatabase.getInstance().getReference()
        var v = inflater.inflate(R.layout.fragment_account, container, false)
        var user_DB_Name = ""
        var user_DB_UserName = ""
        var user_DB_Location = ""

        // Goes through the children of the DB and saves their variables to the user_DB_ variables
        // So we can check them with the user's new variables that they type in.
        db.child("users").child(userID.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.children
                    for (x in value){
                        // This would be the data child, and we aren't working with that here
                        if (x.hasChildren()){
                        }
                        else{
                            if(x.key.toString() == "location"){
                                Log.w("XD", "displayed location")
                                user_DB_Location = x.value.toString()
                                v.findViewById<EditText>(R.id.user_location).setText(x.value.toString())
                            }
                            else if(x.key.toString() == "realName"){
                                Log.w("XD", "displayed name")
                                user_DB_Name = x.value.toString()
                                v.findViewById<EditText>(R.id.user_realName).setText(x.value.toString())
                            }
                            else if(x.key.toString() == "UserName"){
                                Log.w("XD", "displayed USERname")
                                user_DB_UserName = x.value.toString()
                                v.findViewById<EditText>(R.id.user_name).setText(x.value.toString())
                            }

                            else if(x.key.toString() == "favorite_Activities"){
                                listView = v.findViewById<ListView>(R.id.past_activities_list)
                                var favorite_Activities = MutableList<String>(0){""}
                                var current = 0
                                var currentFavoriteActivity = ""
                                for( i in x.key.toString().split(",") ){
                                    current += 1
                                    if(current % 2 == 0){
                                        currentFavoriteActivity +=   " : "+ i

                                        favorite_Activities.add(currentFavoriteActivity)
                                        currentFavoriteActivity = ""
                                    }
                                    else {
                                        currentFavoriteActivity += i

                                    }
                                }
                                // Adds an adapter to actually display the info on the page.
                                var listAdapter =
                                    context?.let { ArrayAdapter<String>(it, R.layout.favorite_activities_in_accounts, R.id.itemTextView,favorite_Activities) }
                                listView.adapter = listAdapter
                            }
                        }
                    }

                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })


        var saveChangesButton = v.findViewById<Button>(R.id.changesSaveButton)
        var currentParam : String = ""

        // Goes through every text field, checks that the field isn't empty & that the user's values
        // aren't the same as before, then writes to db :)
        saveChangesButton.setOnClickListener{

            currentParam = v.findViewById<EditText>(R.id.user_realName).text.toString()
            if(currentParam.isNotEmpty() && currentParam != user_DB_Name){
                Log.w("XD", "Updated the user's name")
                db.child("users").child(userID.toString()).child("realName")
                    .setValue(currentParam)
            }
            currentParam = v.findViewById<EditText>(R.id.user_location).text.toString()
            if(currentParam.isNotEmpty() && currentParam != user_DB_Location){
                Log.w("XD", "Updated the user's location")
                db.child("users").child(userID.toString()).child("location")
                    .setValue(currentParam)
            }

            currentParam = v.findViewById<EditText>(R.id.user_name).text.toString()
            if(currentParam.isNotEmpty() && currentParam != user_DB_UserName){
                Log.w("XD", "Updated the user's userName")
                db.child("users").child(userID.toString()).child("UserName")
                    .setValue(currentParam)
            }

            ///                 Might need to do this one differently because I can't get their passwords :(
            //                  It might be a good idea to make it so they have to check a box before having
            //                  The ability to tap onto the EditText and then change their password.
            currentParam = v.findViewById<EditText>(R.id.user_password).text.toString()
            if(currentParam.isNotEmpty()){
                user?.updatePassword(currentParam)?.addOnSuccessListener {
                    Log.w("XD", "The pass should have changed")
                }
            }

        }

        var image = 0
        db.child("users").child(userID.toString()).child("pfp").get().addOnSuccessListener {
            image = it.value.toString().toInt()
            Log.w("HERE : ", it.value.toString())
            Log.w(image.toString(), R.drawable.pikachupfp.toString())
            var pfpImage = v.findViewById<ImageButton>(R.id.accountPFPInAccount)
            if (image == R.id.Turtle) {
                pfpImage.setImageResource(R.drawable.turtlepfp)
            } else if (image == R.id.Pikachu) {
                pfpImage.setImageResource(R.drawable.pikachupfp)
            } else if (image == R.id.Avatar) {
                pfpImage.setImageResource(R.drawable.avatarpfp)
            } else if (image == R.id.Stitch) {
                pfpImage.setImageResource(R.drawable.stitchpfp)
            } else if (image == R.id.Raze) {
                pfpImage.setImageResource(R.drawable.razepfp)
            } else if (image == R.id.Ponyo) {
                pfpImage.setImageResource(R.drawable.ponyopfp)
            }
        }

        // Reads and displays the user's info on the Account Fragment under friends.
        db.child("users").child(auth.uid.toString()).child("data").child("Friends").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Converts all children (data under Friends) into FriendClass
                val list = dataSnapshot.children.map{ it.getValue(FriendClass :: class.java)!!}
                // Saves their usernames to the List to display in the ListView
                var friends = MutableList<String>(0){""}
                for (x in list){
                    friends.add(x.userName.toString())
                    Log.w("THE", x.userName.toString())
                }
                listView = v.findViewById<ListView>(R.id.friends_list)

                // Adds an adapter to actually display the info on the page.
                var listAdapter =
                    context?.let { ArrayAdapter<String>(it, R.layout.friend_view, R.id.itemTextView,friends) }
                listView.adapter = listAdapter

            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        v.findViewById<EditText>(R.id.friends_list)
        return v
    }
    private fun replaceChildFragment(childFragment : Fragment) {
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(R.id.activities_view, childFragment).addToBackStack(null).commit()
    }
}