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
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
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
import com.google.firebase.database.DataSnapshot
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
    private var adapter: RecyclerView.Adapter<CartActivityAdapter.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_activities, container, false)
        replaceChildFragment(suggestionsFragment) // initial child fragment

        //function for loading backstack page name
        fun getPreviousPageName(vararg params: Void): Void?{
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
                var previousPageName = getChildFragmentManager().getBackStackEntryAt(previousPageCount-1).name.toString()
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
            return null
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
            replaceChildFragment(suggestionsFragment)
            getPreviousPageName()
        }
        coinButton = v.findViewById<Button>(R.id.coin_button)
        coinButton.setOnClickListener{
            replaceChildFragment(coinFragment)
            getPreviousPageName()
        }
        diceButton = v.findViewById<Button>(R.id.dice_button)
        diceButton.setOnClickListener{
            replaceChildFragment(diceFragment)
            getPreviousPageName()
        }
        wheelButton = v.findViewById<Button>(R.id.wheel_button)
        wheelButton.setOnClickListener{
            replaceChildFragment(wheelFragment)
            getPreviousPageName()
        }
        //Mine//
        ///////////////////POP UP IMAGE /////////////////////////////////
        class WebScratch : AsyncTask<Void, Void, Void>() {

            @SuppressLint("ClickableViewAccessibility")
            override fun doInBackground(vararg params: Void): Void? {
                try {
                    //Connect to the website
                    var categoriesToWebscrape = ArrayList<String>()
                    categoriesToWebscrape.add("Cake")
                    categoriesToWebscrape.add("Cars")
                    categoriesToWebscrape.add("Coffee & Tea")
                    categoriesToWebscrape.add("Cookies")
                    categoriesToWebscrape.add("Juice Bars & Smoothies")
                    categoriesToWebscrape.add("Dance Clubs")
                    categoriesToWebscrape.add("Dive Bars")
                    categoriesToWebscrape.add("Dining")
                    categoriesToWebscrape.add("Bowling")
                    categoriesToWebscrape.add("Lounges")
                    categoriesToWebscrape.add("Pizza")
                    categoriesToWebscrape.add("Seafood")
                    var document =
                        Jsoup.connect("https://www.yelp.com/search?cflt="+ categoriesToWebscrape.get(0) +"&find_loc=Long+Beach%2C+CA").get()
                    var infoDocument =
                        Jsoup.connect("https://en.wikipedia.org/wiki/" + categoriesToWebscrape.get(0)).get()

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
                        adapter = CartActivityAdapter(requireContext(), activities)
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
                                    // NNNNN: Add to list to send to RecyclerAdapter class.
                                    activities.add(act)
                                }
                            }
                            // NOTES: v.findViewById<TextView>(R.id.cart_activity_title).text = tempCartActivityText
                            // NOTES: var tempCartActivityText = "Activity 1\nActivity 4\nActivity 10\n"
                            // NNNNN: Update RecyclerAdapter with changes.
                            adapter?.notifyDataSetChanged()
                        }
                        /// MMMMM: Cart Interactability (Swipe) ----------------------------------------------------------------//
                        val swipeGesture = object : SwipeGesture(requireContext()) {
                            override fun onSwiped(
                                viewHolder: RecyclerView.ViewHolder,
                                direction: Int
                            ) {
                                when(direction){
                                    ItemTouchHelper.LEFT -> {
                                        // MMMMM: 1. LEFT Swipe functionality
                                        Log.d("Item Swiped", "${viewHolder.position} Activity")
                                        // NNNNN: 1. delete cartActivity from DATABASE
                                        //Log.d("Item Swiped", "${(adapter as CartActivityAdapter).getItemsId(viewHolder.position)} Activity")
                                        val cartActivityToDeleteID = (adapter as CartActivityAdapter).getItemsId(viewHolder.position)
                                        var cartActivityToDeleteTEMP = activities[0] // initialize if !it.exists()
                                        var cartActivityInfo = db.child("users").child(userID.toString()).get().addOnSuccessListener {
                                            if (it.exists()){
                                                // NOTES: allTheStuff = array of Activities in Cart
                                                val cartActivityToDelete = it.child("data").child("cart").child(cartActivityToDeleteID)
                                                cartActivityToDeleteTEMP = cartActivityToDelete
                                                //Log.d("Deleting", "${cartActivityToDelete} Activity")
                                                cartActivityToDelete.getRef().removeValue()
                                            }
                                            // NOTES: Update RecyclerAdapter with changes.
                                            adapter?.notifyDataSetChanged()
                                        }
                                        // NNNNN: 2. delete cartActivity from VIEW AFTER deleting cartActivity from DATABASE (bc of viewHolder.position)
                                        (adapter as CartActivityAdapter).deleteItem(cartActivityToDeleteTEMP)
                                    }
                                    ItemTouchHelper.RIGHT -> {
                                        // MMMMM: 1. LEFT Swipe functionality
                                        Log.d("Item Swiped", "${viewHolder.position} Activity")
                                        // NNNNN: 1. delete cartActivity from DATABASE
                                        //Log.d("Item Swiped", "${(adapter as CartActivityAdapter).getItemsId(viewHolder.position)} Activity")
                                        val cartActivityToDeleteID = (adapter as CartActivityAdapter).getItemsId(viewHolder.position)
                                        var cartActivityToDeleteTEMP = activities[0] // initialize if !it.exists()
                                        var cartActivityInfo = db.child("users").child(userID.toString()).get().addOnSuccessListener {
                                            if (it.exists()){
                                                // NOTES: allTheStuff = array of Activities in Cart
                                                val cartActivityToDelete = it.child("data").child("cart").child(cartActivityToDeleteID)
                                                cartActivityToDeleteTEMP = cartActivityToDelete
                                                //Log.d("Deleting", "${cartActivityToDelete} Activity")
                                                cartActivityToDelete.getRef().removeValue()
                                            }
                                            // NOTES: Update RecyclerAdapter with changes.
                                            adapter?.notifyDataSetChanged()
                                        }
                                        // NNNNN: 2. delete cartActivity from VIEW AFTER deleting cartActivity from DATABASE (bc of viewHolder.position)
                                        (adapter as CartActivityAdapter).deleteItem(cartActivityToDeleteTEMP)

                                        // XXXXX: 2. Add to Calendar functionality
                                    }
                                }
                            }
                        }
                        val touchHelper = ItemTouchHelper(swipeGesture)
                        touchHelper.attachToRecyclerView(rv)
                        /// MMMMM: -------------------------------------------------------------------------------------------//

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