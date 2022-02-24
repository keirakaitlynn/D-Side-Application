package com.example.dsideapp.childfragments

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.dsideapp.R
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException

class TrailSuggestionsChildFragment : Fragment() {
    private lateinit var textView: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_trail_suggestions_child, container, false)
        textView = v.findViewById(R.id.textViewScrollTrail)
        //----------
        class WebScratch : AsyncTask<Void, Void, Void>() {
            private lateinit var trailName: List<Element>
            private lateinit var trailLinks: MutableList<String>
            private lateinit var imageURL: List<Element>
            private lateinit var formattedFrontEnd: String
            private var counter = 0
            override fun doInBackground(vararg params: Void): Void? {
                try {
                    var inputtedCity = "san+pedro"
                    var searchedURL = "https://www.traillink.com/trailsearch/?city=" + inputtedCity
                    val document =  Jsoup.connect(searchedURL).get()
                    //words = document.text()
                    formattedFrontEnd =  ""
                    trailLinks = mutableListOf()
                    trailName = document.getElementsByClass("small-7 column details")
                    //<a class="xlate-none styles-module__location___hAqkh styles-module__info___jMR_5 styles-module__link___yhpft"
                    // href="/parks/us/california/charles-h-wilson-park?ref=result-card" title="Charles H Wilson Park" style="padding-right: 0px;">Charles H Wilson Park</a>
                    //trailName = document.getElementsByClass("xlate-none styles-module__link___EEZXn")
                    //restaurants = document.getElementsByClass(" css-1e4fdj9")

                    //Image testing
                    var wikiConnect =Jsoup.connect("https://www.yelp.com/search?cflt=bowling&find_loc=Long+Beach%2C+CA").get()
                    imageURL = wikiConnect.getElementsByClass(" css-xlzvdl")
                    //

                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return null
            }
            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                //textView.text = words
                trailName.forEach { element ->
                    //tag "a" gets the trail name
                    formattedFrontEnd += element.getElementsByTag("a").text() + "\n----------------------------\n"
                    var link = element.getElementsByTag("a").attr("href")
                    trailLinks.add(link)
                    counter += 1
                }

                //Testing getting image info
                println("WE AT LEAST GET HERE!")
                var wikiString = ""
                imageURL.forEach { image ->
                    //tag "a" gets the trail name
                    var t = image.absUrl("src")
                    println(t.toString() + "\n")
                }
                //

                textView.text = formattedFrontEnd
                //Print testing if links are being stored
/*                    trailLinks.forEach{
                        l ->
                        println(l.toString())
                    }*/
            }
        }
        WebScratch().execute()
        //----------
        return v
    }
}