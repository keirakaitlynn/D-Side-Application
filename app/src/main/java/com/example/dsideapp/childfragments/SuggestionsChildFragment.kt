package com.example.dsideapp.childfragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException

class SuggestionsChildFragment : Fragment() {
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_child_suggestions, container, false)
        textView = v.findViewById(R.id.textViewScroll)
        //----------
            class WebScratch : AsyncTask<Void, Void, Void>() {
                private lateinit var words: String
                private lateinit var dishName: List<Element>
                private lateinit var prices: List<Element>
                private lateinit var street: List<Element>
                private lateinit var city: List<Element>
                private lateinit var state: List<Element>
                private lateinit var formattedRestaurant: String
                private var counter: Int=0
                override fun doInBackground(vararg params: Void): Void? {
                    try {
                        val document =  Jsoup.connect("https://www.yelp.com/biz/tacos-el-goloso-los-angeles?show_platform_modal=True").get()
                        //words = document.text()
                        formattedRestaurant =  ""
                        dishName = document.getElementsByClass(" css-1peqmen")
                        street = document.getElementsByClass(" css-1bp797t")
                        city = document.getElementsByClass(" css-5588ua")
                        state = document.getElementsByClass(" css-1k57hak")
                        prices = document.getElementsByClass("price__09f24__F1T0p css-dwc5z2")
                        //restaurants = document.getElementsByClass(" css-1e4fdj9")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    return null
                }
                override fun onPostExecute(aVoid: Void?) {
                    super.onPostExecute(aVoid)
                    //textView.text = words
                    formattedRestaurant += street.get(0).text() + " " +
                            city.get(0).text() + " " +
                            state.get(0).text() + "\n------------------\n"
                    prices.forEach { element ->
                        formattedRestaurant += dishName.get(counter).text() + "\n"
                        formattedRestaurant += element.text() + "\n------------------\n"
                        counter += 1
                    }
                    textView.text = formattedRestaurant
                }
            }
            WebScratch().execute()
        //----------
        return v
    }
}