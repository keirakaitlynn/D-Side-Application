
package com.example.dsideapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import android.content.ContentValues.TAG
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.media.Image
import android.widget.*
import android.widget.TextView
import android.widget.Toast
import com.example.dsideapp.data.FriendClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.firebase.auth.ktx.userProfileChangeRequest

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var button: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    lateinit var textView: TextView
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("User")
    var pfp = ""

    fun writeNewUser(userId: String, name: String, email: String, pfp: String) {

        val user = User(name, email, pfp)

        database.reference.child("users").child(userId).setValue(user)

    }
    override fun onCreate(savedInstanceState: Bundle?) {

        // Write a message to the database
        auth = Firebase.auth

        //myRef.setValue("Victor is testing further")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)
                Log.d("Sent", "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Failed", "Failed to read value.", error.toException())
            }
        })

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.sign_up)
        button.setOnClickListener{
            email = findViewById(R.id.emailInput)
            password = findViewById(R.id.passInput)
            createAccount(email.getText().toString().trim(), password.getText().toString().trim())
        }
        //Sending pfp up to db
        var pfpSelectorPonyo = findViewById<ImageButton>(R.id.Ponyo)
        pfpSelectorPonyo.setOnClickListener {
            pfp = R.id.Ponyo.toString()
        }
        var pfpSelectorAvatar = findViewById<ImageButton>(R.id.Avatar)
        pfpSelectorAvatar.setOnClickListener{
            pfp = R.id.Avatar.toString()
        }
        var pfpSelectorRaze = findViewById<ImageButton>(R.id.Raze)
        pfpSelectorRaze.setOnClickListener{
            pfp = R.id.Raze.toString()
        }
        var pfpSelectorPikachu = findViewById<ImageButton>(R.id.Pikachu)
        pfpSelectorPikachu.setOnClickListener{
            pfp = R.id.Pikachu.toString()
        }
        var pfpSelectorStitch = findViewById<ImageButton>(R.id.Stitch)
        pfpSelectorStitch.setOnClickListener{
            pfp = R.id.Stitch.toString()
        }
        var pfpSelectorTurtle = findViewById<ImageButton>(R.id.Turtle)
        pfpSelectorTurtle.setOnClickListener{
            pfp = R.id.Turtle.toString()
        }
        textView = findViewById(R.id.log_in)
        textView.setOnClickListener{
            // KEIRA: Change View from this activity to Log In Activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //val user = User("SuperObama1947", "barackobama42@gmail.com")

        //This will overwrite the data if the user already exists in the db
        //Should probably go in createAccount and only be used if userId doesn't exist in db yet
        //writeNewUser(auth.uid.toString(), "goldfish","goldfish@gmail.com" )
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload()
        }
    }

    private fun reload() {          /// Probably not that important

    }

    /// THE IMPORTANT BITS
    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                var location = findViewById<EditText>(R.id.Location)

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:                    success")
                    var user = Firebase.auth.currentUser
                    var userID = findViewById<EditText>(R.id.displayName)
                    database.reference.child("users").child(auth.currentUser!!.uid.toString()).child("location")
                        .setValue(location.text.toString())
                    //writeNewUser(auth.uid.toString(), email.substring(0,13), email, pfp)
                    Log.w("FUCKKKKKKKKKKKKKK",  userID.text.toString())
                    writeNewUser(auth.uid.toString(), email.substring(0,13), email, pfp)
                    val x = FriendClass(user!!.email.toString(), user!!.uid.toString(), userID.text.toString() )
                    if (user != null) {
                        database.reference.child("Phonebook").child(user.email.toString().split("@")[0]).setValue(x)
                    }


                    val profileUpdates = userProfileChangeRequest {
                        displayName = userID.toString() //put in the account field info here
                    }

                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User profile updated.")
                            }
                        }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        // [END create_user_with_email]
    }

    /// THE IMPORTANT BITS
    private fun logIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = Firebase.auth.currentUser
                    user?.let {
                        // Name, email address, and profile photo Url
                        val name = user.displayName
                        val email = user.email
                        val photoUrl = user.photoUrl

                        // Check if user's email is verified
                        val emailVerified = user.isEmailVerified

                        // The user's ID, unique to the Firebase project. Do NOT use this value to
                        // authenticate with your backend server, if you have one. Use
                        // FirebaseUser.getToken() instead.
                        val uid = user.uid
                    }
                    Log.w("IDK ", user?.email.toString())

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }
    data class User(val username: String? = null, val email: String? = null, val pfp: String? = null) {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }
}