package com.example.dsideapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class LogoutActivity : AppCompatActivity() {

    lateinit var button: Button
    lateinit var textView: TextView

    override fun onCreate(savedInstaceState: Bundle?) {
        super.onCreate(savedInstaceState)
        setContentView(R.layout.activity_logout)

        FirebaseAuth.getInstance()

        button = findViewById(R.id.log_out)
        //textView = findViewById(R.id.logged_in)

        //textView.setText("Welcome")

        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

//    private fun openNewActivity() {
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivity(intent)
//    }

}