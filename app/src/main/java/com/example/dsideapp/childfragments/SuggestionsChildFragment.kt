package com.example.dsideapp.childfragments

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.data.*
import com.google.firebase.database.FirebaseDatabase
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import kotlin.random.Random

//Main
private const val TAG = "MainActivity"                              // Used to debug
private const val BASE_URL = "https://api.yelp.com/v3/"             // The base URL from which we will use to communicate with Yelp 🙂
private const val API_KEY = "aw6IUY4GfgjgDLrCrYzQQTYLmopzS5y-_G8u3KbuI3ksuVNdRsQwWHf-7_Z4ROT9ws68DbgFDP1amLP0lLDVjcDnVzzUKg7HU9y7LKfPHoZWoyhKbn1LjnZh2FoAYnYx"

class SuggestionsChildFragment : Fragment() {
    var s: ArrayList<String>? = null
    var arrayAdapter: ArrayAdapter<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_child_suggestions, container, false)

        s = ArrayList()
        s!!.add("ONE")
        s!!.add("TWO")
        s!!.add("THREE")
        s!!.add("FOUR")
        s!!.add("FIVE")
        s!!.add("SIX")
        s!!.add("SEVEN")
        val swipeFlingAdapterView = v.findViewById<View>(R.id.card) as SwipeFlingAdapterView
        // Must use requireContext() vs. "this" to get a Context because a Fragment is not a Context.
        arrayAdapter = ArrayAdapter<String>(requireContext(), R.layout.details, R.id.textView, s!!)
        swipeFlingAdapterView.adapter = arrayAdapter
        swipeFlingAdapterView.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                s!!.removeAt(0)
                (arrayAdapter as ArrayAdapter<String>).notifyDataSetChanged()
            }
            override fun onLeftCardExit(o: Any) {}
            override fun onRightCardExit(o: Any) {}
            override fun onAdapterAboutToEmpty(i: Int) {}
            override fun onScroll(v: Float) {}
        })
        // Yelp API ---------------
//        val rvRestaurants = v.findViewById<RecyclerView>(R.id.rvRestaurants)
//        val restaurants = mutableListOf<YelpRestaurant>()
//        val adapter =
//            activity?.let { RestaurantsAdapter(it.applicationContext, restaurants) }
//        rvRestaurants.adapter = adapter
//        rvRestaurants.layoutManager = LinearLayoutManager(activity?.applicationContext) // Vertical linear layout
//
//        val retrofit =
//            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
//                .build()
//        val yelpService = retrofit.create(YelpService::class.java)                  // A filter just reduces # of results due to specificity
//        yelpService.searchRestaurants("Bearer $API_KEY", null, null,"bars",null,"1",null,null,null,"Los Angeles", ).enqueue(object :
//            Callback<YelpSearchResult> {
//            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
//                Log.i(TAG, "onResponse $response")
//                val body = response.body()
//               // Log.w("\n\nHELP\n\n", body.toString())
//                if (body == null) {
//                    Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
//                    return
//                }
//                restaurants.addAll(body.restaurants)
//
//                adapter?.notifyDataSetChanged()
//                Log.w(TAG, "Done")
//            }
//
//            override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
//                Log.i(TAG, "onFailure $t")
//            }
//        })
        // -------------

        return v
    }
}