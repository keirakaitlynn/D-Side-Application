package com.example.dsideapp.childfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.data.RestaurantsAdapter
import com.example.dsideapp.data.YelpRestaurant
import com.example.dsideapp.data.YelpSearchResult
import com.example.dsideapp.data.YelpService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"                              // Used to debug
private const val BASE_URL = "https://api.yelp.com/v3/"             // The base URL from which we will use to communicate with Yelp 🙂
private const val API_KEY = "aw6IUY4GfgjgDLrCrYzQQTYLmopzS5y-_G8u3KbuI3ksuVNdRsQwWHf-7_Z4ROT9ws68DbgFDP1amLP0lLDVjcDnVzzUKg7HU9y7LKfPHoZWoyhKbn1LjnZh2FoAYnYx"

class SuggestionsChildFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_child_suggestions, container, false)

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
                if (body == null) {
                    Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                    return
                }
                restaurants.addAll(body.restaurants)
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