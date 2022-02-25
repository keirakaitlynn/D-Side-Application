package com.example.dsideapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.FirebaseAuth
import android.view.Gravity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.*

import com.example.dsideapp.auth
import com.example.dsideapp.childfragments.*
import com.example.dsideapp.data.ActivityObject
import com.example.dsideapp.data.LocationObject
import com.google.firebase.database.FirebaseDatabase
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException
import java.io.InputStream
import kotlin.random.Random

class ActivitiesFragment : Fragment() {
    lateinit var suggestionsButton : Button
    lateinit var coinButton : Button
    lateinit var diceButton : Button
    lateinit var wheelButton : Button

    private val suggestionsFragment = SuggestionsChildFragment()
    private val coinFragment = CoinChildFragment()
    private val diceFragment = DiceChildFragment()
    private val wheelFragment = WheelChildFragment()
    //Main
    //private lateinit var auth: FirebaseAuth
    //Mine
    private val concertsFragment = ConcertsFragment()
    private val trailSuggestionsFragment = TrailSuggestionsChildFragment()
    private lateinit var cartPopUpText: TextView
    private lateinit var infoPopUpText: TextView
    //Popup information image variables
    private lateinit var cartImg: Element
    private lateinit var cartImgSrc: String
    private lateinit var cartInput: InputStream
    private lateinit var cartBitmap: Bitmap
    private lateinit var cartImageView: ImageView
    //Cart image variables
    private lateinit var infoImg: Element
    private lateinit var infoImgSrc: String
    private lateinit var infoInput: InputStream
    private lateinit var infoBitmap: Bitmap
    private lateinit var infoImageView: ImageView

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
            replaceChildFragment(wheelFragment)
        }
        //Mine//
        ///////////////////POP UP IMAGE /////////////////////////////////
        class WebScratch : AsyncTask<Void, Void, Void>() {

            @SuppressLint("ClickableViewAccessibility")
            override fun doInBackground(vararg params: Void): Void? {
                try {
                    //Connect to the website
                    var document =
                        Jsoup.connect("https://www.yelp.com/search?cflt=bowling&find_loc=Long+Beach%2C+CA").get()
                    var infoDocument =
                        Jsoup.connect("https://en.wikipedia.org/wiki/Bowling").get()

                    //////////
                    ///Cart image info gathering
                    //Get the logo source of the website
                    infoImg = infoDocument.getElementsByTag("img").first()!!
                    // Locate the src attribute
                    infoImgSrc = infoImg.absUrl("src")
                    println("\n\n" + infoImgSrc + "\n\n")
                    //Download image from URL
                    infoInput = java.net.URL(infoImgSrc).openStream()
                    // Decode Bitmap
                    infoBitmap = BitmapFactory.decodeStream(infoInput)
                    ///
                    val infoDescription = infoDocument.select("p")
                    val infoButton = v.findViewById<ImageButton>(R.id.info_button)
                    infoButton.setOnClickListener{
                        // inflate the layout of the popup window
                        v = inflater.inflate(com.example.dsideapp.R.layout.fragment_info_pop_up, null)
                        // create the popup window
                        val width = LinearLayout.LayoutParams.WRAP_CONTENT
                        val height = LinearLayout.LayoutParams.WRAP_CONTENT
                        val focusable = true // lets taps outside the popup also dismiss it
                        val popupWindow = PopupWindow(v, width, height, focusable)

                        //Popup window for the info
                        infoPopUpText = v.findViewById(R.id.popUpTextInfo)
                        if (infoDescription != null) {
                            infoPopUpText.text = infoDescription.get(1).text().toString()
                        }
                        infoImageView = v.findViewById(R.id.popUpImageInfo)
                        //textView = findViewById(R.id.title)
                        infoImageView.setImageBitmap(infoBitmap)

                        // show the popup window
                        // which view you pass in doesn't matter, it is only used for the window token
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                        v.setOnTouchListener { v, event ->
                            popupWindow.dismiss()
                            true
                        }
                    }
                    //////////////

                    ///Cart image info gathering
                    //Get the logo source of the website
                    cartImg = document.getElementsByTag("img").first()!!
                    // Locate the src attribute
                    cartImgSrc = cartImg.absUrl("src")
                    println("\n\n" + cartImgSrc + "\n\n")
                    //title = img.attr("alt")
                    //Download image from URL
                    cartInput = java.net.URL(cartImgSrc).openStream()
                    // Decode Bitmap
                    cartBitmap = BitmapFactory.decodeStream(cartInput)
                    ///
                    //Get the title of the website
                    //title = document.title()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return null
            }

            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
            }
        }
        /////////////////////////////////////////////////////////////////
        //cart pop-up window on button click/
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
            //getting the db info for popup
            //var tempTestText = "Activity 1\nActivity 4\nActivity 10\n"
            var activityInfo = db.child("users").child(userID.toString()).get().addOnSuccessListener {
            //Popup window for the cart
            cartPopUpText = v.findViewById(R.id.popUpText)
                if (it.exists()){
                    cartPopUpText.text = it.child("data").child("activities").child("1233abc").child("title").value.toString()
                    cartImageView = v.findViewById(R.id.popUpImage)
                    //textView = findViewById(R.id.title)
                    cartImageView.setImageBitmap(cartBitmap)
                }
            }
            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            v.setOnTouchListener { v, event ->
                popupWindow.dismiss()
                true
            }

        }
        WebScratch().execute()
        return v
    }

    private fun replaceChildFragment(childFragment : Fragment) {
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(R.id.activities_view, childFragment).commit()
    }
//Main
    data class Activity(val username: String? = null, val location: String? = null, val date_time: String? = null) {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }

}