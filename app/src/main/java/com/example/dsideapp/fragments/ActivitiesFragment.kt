package com.example.dsideapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.R
import com.example.dsideapp.childfragments.*
import android.view.MotionEvent

import android.view.Gravity

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.util.Log
import android.view.View.OnTouchListener
import android.widget.*
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService

import com.example.dsideapp.LoginActivity
import com.example.dsideapp.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class ActivitiesFragment : Fragment() {
    lateinit var suggestionsButton : Button
    lateinit var coinButton : Button
    lateinit var diceButton : Button
    lateinit var wheelButton : Button
    private lateinit var popUpText: TextView

    private val suggestionsFragment = SuggestionsChildFragment()
    private val coinFragment = CoinChildFragment()
    private val diceFragment = DiceChildFragment()
    private val wheelFragment = WheelChildFragment()
    private val concertsFragment = ConcertsFragment()
    private val trailSuggestionsFragment = TrailSuggestionsChildFragment()
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_activities, container, false)
        replaceChildFragment(suggestionsFragment) // initial child fragment

        suggestionsButton = v.findViewById<Button>(R.id.suggestions_button)
        suggestionsButton.setOnClickListener{
            replaceChildFragment(suggestionsFragment)
        }
        coinButton = v.findViewById<Button>(R.id.coin_button)
        coinButton.setOnClickListener{
            replaceChildFragment(coinFragment)
        }
        diceButton = v.findViewById<Button>(R.id.dice_button)
        diceButton.setOnClickListener{
            replaceChildFragment(diceFragment)
        }
        wheelButton = v.findViewById<Button>(R.id.wheel_button)
        wheelButton.setOnClickListener{
            //Temporarily have this in concert fragment to avoid code clash
            replaceChildFragment(concertsFragment)

            //replaceChildFragment(wheelFragment)
        }

        //cart pop-up window on button click
        val cartButton = v.findViewById<ImageButton>(R.id.cart_button)
        cartButton.setOnClickListener{
            // inflate the layout of the popup window
            v = inflater.inflate(com.example.dsideapp.R.layout.fragment_cart_pop_up, null)
            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(v, width, height, focusable)

            //getting database info
            var authorization = auth
            var user = authorization.currentUser
            var userID = authorization.currentUser?.uid
            var db = FirebaseDatabase.getInstance().getReference()
            //getting the db info
            var tempTestText = "Activity 1\nActivity 4\nActivity 10\n"
            var emailInfo = db.child("users").child(userID.toString()).get().addOnSuccessListener {
            popUpText = v.findViewById(R.id.popUpText)
                if (it.exists()){
                    popUpText.text = it.child("email").value.toString()
                }
            }
            println(tempTestText)
            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            // dismiss the popup window when touched
            //popUpText = v.findViewById(R.id.popUpText)
            //popUpText.text = tempTestText
            v.setOnTouchListener { v, event ->
                popupWindow.dismiss()
                true
            }
        }
        return v
    }

    private fun replaceChildFragment(childFragment : Fragment) {
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(R.id.activities_view, childFragment).commit()
    }
}