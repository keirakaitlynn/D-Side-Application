

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
import android.widget.TextView
import android.widget.Toast
import com.example.dsideapp.data.FriendClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

var auth: FirebaseAuth = Firebase.auth
val selectedItemsForDecisionTools = emptyMap<DataSnapshot, Int>()

class LoginActivity : AppCompatActivity() {

    lateinit var button: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        // Write a message to the database
        auth = Firebase.auth
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("User")

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
        setContentView(R.layout.activity_login)

        button = findViewById(R.id.log_in)
        button.setOnClickListener{
            email = findViewById(R.id.emailInput)
            password = findViewById(R.id.passInput)
            logIn(email.getText().toString().trim(), password.getText().toString().trim())
        }

        textView = findViewById(R.id.sign_up)
        textView.setOnClickListener {
            // KEIRA: Change View from this activity to Main Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        textView = findViewById(R.id.recover_email)
        textView.setOnClickListener {
            email = findViewById(R.id.emailInput)
            Firebase.auth.sendPasswordResetEmail(email.getText().toString().trim())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "Email sent.",
                            Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Email sent.")
                    } else {
                        Toast.makeText(baseContext, "Email was not sent.",
                            Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Email not sent.")
                    }
                }.addOnFailureListener {
                    Toast.makeText(baseContext, "Email was not sent.",
                        Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Email not sent.")
                }
        }
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
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:                    success")
                    val user = auth.currentUser

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
        println("Email: " + email)
        println("Password: " + password)
        println(auth.currentUser.toString())
        // [START sign_in_with_email]

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
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

                    // KEIRA: Change View from this activity to Home Activity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }
}