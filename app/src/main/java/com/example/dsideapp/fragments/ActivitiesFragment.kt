package com.example.dsideapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.R
import com.example.dsideapp.childfragments.*
import android.view.MotionEvent

import android.view.Gravity

import android.widget.PopupWindow

import android.widget.LinearLayout

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.View.OnTouchListener
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService




class ActivitiesFragment : Fragment() {
    lateinit var suggestionsButton : Button
    lateinit var coinButton : Button
    lateinit var diceButton : Button
    lateinit var wheelButton : Button

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
        val v = inflater.inflate(R.layout.fragment_activities, container, false)
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
            val popupView: View = inflater.inflate(com.example.dsideapp.R.layout.fragment_cart_pop_up, null)
            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(popupView, width, height, focusable)
            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            // dismiss the popup window when touched
            popupView.setOnTouchListener { v, event ->
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