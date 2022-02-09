package com.example.dsideapp.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.R
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException

class SuggestionsFragment : Fragment() {
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_suggestions, container, false)
        textView = v.findViewById(R.id.textViewScroll)
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
        //----------
        return v
    }
}