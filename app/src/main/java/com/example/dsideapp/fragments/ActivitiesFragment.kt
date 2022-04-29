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
import android.view.Gravity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.AsyncTask.execute
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.core.widget.TextViewCompat.setTextAppearance
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.dsideapp.auth
import com.example.dsideapp.childfragments.*
import com.google.firebase.database.FirebaseDatabase
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException
import java.io.InputStream

import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.HomeActivity
import com.example.dsideapp.data.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.ktx.Firebase
import org.jsoup.nodes.Document
import java.util.ArrayList


class ActivitiesFragment : Fragment() , HomeActivity.IOnBackPressed {
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
    private val pollsFragment = PollsFragment()
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
    private lateinit var rvRestaurants: RecyclerView
    private lateinit var ppw: PopupWindow
    // MMMMM: RecyclerView + CardView (ActivitiesFragment, CartPopUpFragment)
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_activities, container, false)
        replaceChildFragment(suggestionsFragment) // initial child fragment

        //function for loading backstack page name
        fun getPreviousPageName(vararg params: Void): String{
            var previousPageName = ""
            if (getChildFragmentManager().backStackEntryCount > 1){
                ppw.dismiss()
            }
            //---- BackStack Name ----
            // create the popup window
            v = inflater.inflate(com.example.dsideapp.R.layout.back_stack_name, null)
            var previousPageTextView = v.findViewById<TextView>(R.id.previous_page)
            //getting name of previous page
            var previousPageCount = getChildFragmentManager().backStackEntryCount
            if (previousPageCount >1){
                previousPageName = getChildFragmentManager().getBackStackEntryAt(previousPageCount-1).name.toString()
                //Checks if page count updates
                //Log.w("Page: ",
                //    getChildFragmentManager().getBackStackEntryAt(previousPageCount-1).name.toString())
                previousPageTextView.text = previousPageName
            }

            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = false // lets taps outside the popup also dismiss it
            ppw = PopupWindow(v, width, height, focusable)
            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            ppw.showAtLocation(view, Gravity.NO_GRAVITY, 140, 0)

            v.setOnTouchListener { v, event ->
                ppw.dismiss()
                true
            }
            //---- BackStack Name ----
            return previousPageName
        }

        //function for loading backstack page name
        fun getPreviousPageNameBackButton(vararg params: Void): Void?{
            if (getChildFragmentManager().backStackEntryCount > 1){
                ppw.dismiss()
            }
            if (getChildFragmentManager().backStackEntryCount>2) {
                //---- BackStack Name ----
                // create the popup window
                v = inflater.inflate(com.example.dsideapp.R.layout.back_stack_name, null)
                var previousPageTextView = v.findViewById<TextView>(R.id.previous_page)
                //getting name of previous page
                var previousPageCount = getChildFragmentManager().backStackEntryCount
                if (previousPageCount > 2) {
                    var previousPageName =
                        getChildFragmentManager().getBackStackEntryAt(previousPageCount - 3).name.toString()
                    //Checks if page count updates
                    Log.w(
                        "Page: ",
                        getChildFragmentManager().getBackStackEntryAt(previousPageCount - 3).name.toString()
                    )
                    previousPageTextView.text = previousPageName
                }

                val width = LinearLayout.LayoutParams.WRAP_CONTENT
                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val focusable = false // lets taps outside the popup also dismiss it
                ppw = PopupWindow(v, width, height, focusable)
                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window token
                ppw.showAtLocation(view, Gravity.NO_GRAVITY, 140, 0)

                v.setOnTouchListener { v, event ->
                    ppw.dismiss()
                    true
                }
            }
            //---- BackStack Name ----
            return null
        }

        suggestionsButton = v.findViewById<Button>(R.id.suggestions_button)
        suggestionsButton.setOnClickListener{
            setTextAppearance(suggestionsButton, R.style.button_page_selected)
            //Unga bunga way of doing this, but it works lol
            setTextAppearance(coinButton, R.style.button_page)
            setTextAppearance(diceButton, R.style.button_page)
            setTextAppearance(wheelButton, R.style.button_page)
            replaceChildFragment(suggestionsFragment)
        }
        coinButton = v.findViewById<Button>(R.id.coin_button)
        coinButton.setOnClickListener{
            setTextAppearance(coinButton, R.style.button_page_selected)
            //Unga bunga way of doing this, but it works lol
            setTextAppearance(suggestionsButton, R.style.button_page)
            setTextAppearance(diceButton, R.style.button_page)
            setTextAppearance(wheelButton, R.style.button_page)
            replaceChildFragment(coinFragment)
        }
        diceButton = v.findViewById<Button>(R.id.dice_button)
        diceButton.setOnClickListener{
            setTextAppearance(diceButton, R.style.button_page_selected)
            //Unga bunga way of doing this, but it works lol
            setTextAppearance(suggestionsButton, R.style.button_page)
            setTextAppearance(coinButton, R.style.button_page)
            setTextAppearance(wheelButton, R.style.button_page)
            replaceChildFragment(diceFragment)
        }
        wheelButton = v.findViewById<Button>(R.id.wheel_button)
        wheelButton.setOnClickListener{
            setTextAppearance(wheelButton, R.style.button_page_selected)
            //Unga bunga way of doing this, but it works lol
            setTextAppearance(suggestionsButton, R.style.button_page)
            setTextAppearance(coinButton, R.style.button_page)
            setTextAppearance(diceButton, R.style.button_page)
            replaceChildFragment(wheelFragment)
        }
        //Mine//
        ///////////////////POP UP IMAGE /////////////////////////////////

        class WebScratchUpdater : AsyncTask<String, Void, List<Document>>(){
            @SuppressLint("ClickableViewAccessibility")
            override fun doInBackground(vararg params: String): List<Document>? {
                try {
                    //Initial Jsoup variables.
                    //yelp_webscraper is used to keep the url updating simple.

                    Log.w("infobutton", "entered doinbackground")
                    var connection = Jsoup.connect("https://www.yelp.com/search?cflt=" + params.first() + "&find_loc=Long+Beach%2C+CA")
                    var document = connection.get()
                    var infoConnection = Jsoup.connect("https://en.wikipedia.org/wiki/" + params.first())
                    var infoDocument = infoConnection.get()
                    var toreturn = listOf(document, infoDocument)
                    Log.w("infobutton", "entered Jsoup connections")

                    infoImg = infoDocument.getElementsByTag("img").first()!!
                    // Locate the src attribute
                    infoImgSrc = infoImg.absUrl("src")
                    println("\n\n" + infoImgSrc + "\n\n")
                    //Download image from URL
                    infoInput = java.net.URL(infoImgSrc).openStream()
                    // Decode Bitmap
                    infoBitmap = BitmapFactory.decodeStream(infoInput)
                    Log.w("infobutton", "downloaded images and decoded bitmaps")

                    val infoDescription = infoDocument.select("p")

                    // inflate the layout of the popup window
                    var vPop = inflater.inflate(com.example.dsideapp.R.layout.fragment_info_pop_up, null)
                    // create the popup window
                    val width = LinearLayout.LayoutParams.WRAP_CONTENT
                    val height = LinearLayout.LayoutParams.WRAP_CONTENT
                    val focusable = true // lets taps outside the popup also dismiss it
                    val popupWindow = PopupWindow(vPop, width, height, focusable)
                    Log.w("infobutton", "inflated and reacted popupWindow")

                    //Popup window for the info
                    infoPopUpText = vPop.findViewById(R.id.popUpTextInfo)
                    if (infoDescription != null) {
                        infoPopUpText.text = infoDescription.get(1).text().toString()
                    }
                    infoImageView = vPop.findViewById(R.id.popUpImageInfo)
                    //textView = findViewById(R.id.title)
                    infoImageView.setImageBitmap(infoBitmap)

                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window token
                    Log.w("infobutton", "Prepared to do popupWindow")
                    Handler(Looper.getMainLooper()).post {
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                    }

                    Log.w("infobutton", "Popupwindow showd at location")
                    v.setOnTouchListener { v, event ->
                        popupWindow.dismiss()
                        true
                    }
                    return toreturn
                    //
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return null
            }

            override fun onPostExecute(result: List<Document>) {
                super.onPostExecute(result)
            }
        }
        class WebScratch : AsyncTask<Void, Void, Void>() {

            @SuppressLint("ClickableViewAccessibility")
            override fun doInBackground(vararg params: Void): Void? {
                try {
                    val auth = Firebase.auth
                    val database = FirebaseDatabase.getInstance()
                    //Initial Jsoup variables.
                    //yelp_webscraper is used to keep the url updating simple.
                    var yelp_webscraper = arrayListOf("https://www.yelp.com/search?cflt=", "Recreation", "&find_loc=Long+Beach%2C+CA")
                    var connection = Jsoup.connect(yelp_webscraper[0] + yelp_webscraper[1] + yelp_webscraper[2])
                    var document = connection.get()
                    var infoConnection = Jsoup.connect("https://en.wikipedia.org/wiki/" + yelp_webscraper[1])
                    var infoDocument = infoConnection.get()
                    //


                    //Update webscraper using new keyword "Cake" through changing URL and getting connection.

                    database.reference.child("users").child(auth.uid.toString()).child("data").child("curr_category").get().addOnSuccessListener {
                        yelp_webscraper[1] = it.value.toString()
                        Log.w("Help me", yelp_webscraper[1])
                    }
                    connection.url(yelp_webscraper[0] + yelp_webscraper[1] + yelp_webscraper[2])
                    document = connection.get()
                    infoConnection.url("https://en.wikipedia.org/wiki/" + yelp_webscraper[1])
                    infoDocument = infoConnection.get()
                    //////////
                    ///Info image info gathering
                    //Get the logo source of the website
                    infoImg = infoDocument.getElementsByTag("img").first()!!
                    // Locate the src attribute
                    infoImgSrc = infoImg.absUrl("src")
                    println("\n\n" + infoImgSrc + "\n\n")
                    //Download image from URL
                    infoInput = java.net.URL(infoImgSrc).openStream()
                    // Decode Bitmap
                    infoBitmap = BitmapFactory.decodeStream(infoInput)

                    val infoDescription = infoDocument.select("p")
                    val infoButton = v.findViewById<ImageButton>(R.id.info_button)

                    infoButton.setOnClickListener{
                        Log.w("infobutton", "clicked")
                        database.reference.child("users").child(auth.uid.toString()).child("data").child("curr_category").get().addOnSuccessListener {
                            var documents = WebScratchUpdater().execute(it.value.toString())
                            Log.w("info", documents.toString())
                        }.addOnFailureListener {
                            Log.w("failure","rip")
                        }
                    }
                    //////////////
                    //Setting the text for previous page, shown next to back button

                    val backButton = v.findViewById<ImageButton>(R.id.back_button)
                    backButton.setOnClickListener{
                        getPreviousPageNameBackButton()
                        Log.w("Num: ", getChildFragmentManager().backStackEntryCount.toString())
                        if (getChildFragmentManager().backStackEntryCount == 2){
                            ppw.dismiss()
                        }
                        onBackPressed()
                    }

                    //// MMMMM: ====================================================================
                    /// -----------
                    val searchButton = v.findViewById<Button>(R.id.search_button)
                    searchButton.setOnClickListener{
                        //// NNNNN: ====================================================================
                        val searchView = v.findViewById<SearchView>(R.id.searchView)
                        //val listView = v.findViewById<ListView>(R.id.listView)
                        //val names = arrayOf("Android", "Java", "Php", "Python", "C", "C++", "Kotlin")

                        var categories = ArrayList<String>()
                        categories.add("Cake")
                        categories.add("Cars")
                        categories.add("Coffee & Tea")
                        categories.add("Cookies")
                        categories.add("Juice Bars & Smoothies")
                        categories.add("Dance Clubs")
                        categories.add("Dive Bars")
                        categories.add("Dining")
                        categories.add("Bowling")
                        categories.add("Lounges")
                        categories.add("Pizza")
                        categories.add("Seafood")

                        val categoryRecyclerView = v.findViewById<RecyclerView>(R.id.categoryRecyclerView)
                        val categoryAdapter = CategoryAdapter(categories)
                        categoryRecyclerView.setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))
                        categoryRecyclerView.setAdapter(categoryAdapter)

                        categoryRecyclerView.setNestedScrollingEnabled(false);

                        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                            override fun onQueryTextChange(newText: String?): Boolean {
                                categoryAdapter.filter.filter(newText)
                                return false
                            }

                            override fun onQueryTextSubmit(s: String?): Boolean {
                                Log.w("data",":) :" + s)
                                Log.w("Change was detected","!")
                                val fragmentManager = getActivity()?.getSupportFragmentManager()
                                if (fragmentManager != null) {
                                    fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.activities_view,  SuggestionsChildFragment()).commit()
                                }
                                Log.w("data",":( :" + s)
                                return false
                            }
                        })
                        //// NNNNN: ====================================================================
                    }

                    //// MMMMM: ====================================================================

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

                        // MMMMM: RecyclerView + CardView (ActivitiesFragment, CartPopUpFragment) -//
                        val rv = v.findViewById<RecyclerView>(R.id.recyclerView)
                        val activities = mutableListOf<DataSnapshot>()
                        layoutManager = LinearLayoutManager(requireContext())
                        rv.layoutManager = layoutManager
                        adapter = RecyclerAdapter(requireContext(), activities)
                        rv.adapter = adapter
                        // MMMMM: -----------------------------------------------------------------//
                        var activityInfo = db.child("users").child(userID.toString()).get().addOnSuccessListener {
                            //Popup window for the cart
                            var tempCartActivityText = ""
                            if (it.exists()){
                                // NOTES: allTheStuff = array of Activities in Cart
                                val allTheStuff = it.child("data").child("cart").children
                                allTheStuff.forEach{
                                    act ->
                                    tempCartActivityText += act.child("title").value.toString() + "\n"
                                    // MMMMM: Add to list to send to RecyclerAdapter class.
                                    activities.add(act)
                                }
                            }
                            // NOTES: v.findViewById<TextView>(R.id.cart_activity_title).text = tempCartActivityText
                            // NOTES: var tempCartActivityText = "Activity 1\nActivity 4\nActivity 10\n"
                            // MMMMM: Update RecyclerAdapter with changes.
                            adapter?.notifyDataSetChanged()
                        }
                        // show the popup window
                        // which view you pass in doesn't matter, it is only used for the window token
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                        v.setOnTouchListener { v, event ->
                            popupWindow.dismiss()
                            true
                        }

                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return null
            }
        }
        WebScratch().execute()
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun replaceChildFragment(childFragment : Fragment) {
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        //Naming the fragment
        var fragName = ""
        if (childFragment == coinFragment){
            fragName = "Coin"
        }
        else if (childFragment == diceFragment){
            fragName = "Dice"
        }
        else if (childFragment == wheelFragment){
            fragName = "Wheel"
        }
        else{
            fragName = "Suggestion"
        }
        transaction.replace(R.id.activities_view, childFragment).addToBackStack(fragName).commit()
    }
    //Main
    data class Activity(val username: String? = null, val location: String? = null, val date_time: String? = null) {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }


    override fun onBackPressed(): Boolean {
        //Log.d(ContentValues.TAG, "onBackPressed:success")
        return if (getChildFragmentManager().getBackStackEntryCount() >= 2) {
            getChildFragmentManager().popBackStackImmediate()
            true
        } else {
            false
        }
    }
}