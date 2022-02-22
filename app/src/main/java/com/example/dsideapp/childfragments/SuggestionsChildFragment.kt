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
            private lateinit var trailName: List<Element>
            private lateinit var trailLinks: MutableList<String>
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