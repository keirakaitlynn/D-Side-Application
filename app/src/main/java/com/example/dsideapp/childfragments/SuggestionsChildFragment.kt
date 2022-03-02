package com.example.dsideapp.childfragments

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.data.*
import com.google.firebase.database.FirebaseDatabase
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

//Main
private const val TAG = "MainActivity"                              // Used to debug
private const val BASE_URL = "https://api.yelp.com/v3/"             // The base URL from which we will use to communicate with Yelp 🙂
private const val API_KEY = "aw6IUY4GfgjgDLrCrYzQQTYLmopzS5y-_G8u3KbuI3ksuVNdRsQwWHf-7_Z4ROT9ws68DbgFDP1amLP0lLDVjcDnVzzUKg7HU9y7LKfPHoZWoyhKbn1LjnZh2FoAYnYx"

class SuggestionsChildFragment : Fragment() {
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_child_suggestions, container, false)
/*        textView = v.findViewById(R.id.textViewScroll)
           //----------
            class WebScratch : AsyncTask<Void, Void, Void>() {
                private lateinit var words: String
                private lateinit var restaurants: List<Element>
                private lateinit var formattedRestaurant: String
                override fun doInBackground(vararg params: Void): Void? {
                    try {
                        val document =  Jsoup.connect("https://www.yelp.com/search?find_desc=taco&find_loc=Carson%2C+CA").get()
                        words = document.text()
                        formattedRestaurant =  ""
                        restaurants = document.getElementsByClass(" css-1e4fdj9")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    return null
                }
                override fun onPostExecute(aVoid: Void?) {
                    super.onPostExecute(aVoid)
                    //textView.text = words
                    restaurants.forEach { element ->
                        formattedRestaurant += element.text() + "\n------------------\n"
                    }
                    textView.text = formattedRestaurant
                }
            }
            WebScratch().execute()
        //----------*/
        //Main

        // Yelp API ---------------
        val rvRestaurants = v.findViewById<RecyclerView>(R.id.rvRestaurants)
        val restaurants = mutableListOf<YelpRestaurant>()
        val adapter =
            activity?.let { RestaurantsAdapter(it.applicationContext, restaurants) }
        rvRestaurants.adapter = adapter
        rvRestaurants.layoutManager = LinearLayoutManager(activity?.applicationContext) // Vertical linear layout

        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        val yelpService = retrofit.create(YelpService::class.java)                  // A filter just reduces # of results due to specificity
        yelpService.searchRestaurants("Bearer $API_KEY", null, null,"bars",null,"1",null,null,null,"Los Angeles", ).enqueue(object :
            Callback<YelpSearchResult> {
            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()
               // Log.w("\n\nHELP\n\n", body.toString())
                if (body == null) {
                    Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                    return
                }
                restaurants.addAll(body.restaurants)

                ///Save the cart info into the db temporarily
                //Creating db references
                var authorization = auth
                var user = authorization.currentUser
                var userID = authorization.currentUser?.uid
                var db = FirebaseDatabase.getInstance().getReference()
                //Creating writeToDB function
                fun writeNewActivity(userId: String, id: String, title: String = "None", phone: String = "None",
                                     image: String = "None", loc_address: String = "None", loc_city: String = "None",
                                     loc_country:String = "None", loc_zip: String = "None", loc_state: String = "None"
                                     , business_name: String = "None", price: String = "None") {
                    val location = LocationObject(loc_address, loc_city, loc_country, loc_zip, loc_state)
                    val activity = ActivityObject(if(id != "") id else "null", title, phone, image, location, business_name, price)
                    db.child("users").child(userId).child("data").child("activities").child(if(id != "") id else "null").setValue(activity)
                }
                restaurants.forEach{ rest->
                    //Create random ID tag
                    var i = 0
                    var randID = ""
                    for(i in 1..3){
                        randID += Random.nextInt(9)
                    }
                    for(i in 1..3){
                        randID += (Random.nextInt(25) + 65).toChar()
                    }
                    //Actually saving activity to db
                    writeNewActivity(userId = userID.toString(), id = randID, title = rest.categories.get(0).toString(),
                        image = rest.imageUrl, business_name = rest.name, price = rest.price)
                }
                ///

                adapter?.notifyDataSetChanged()
                Log.w(TAG, "Done")
            }

            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                Log.i(TAG, "onFailure $t")
            }
        })
        // -------------

        return v
    }
}