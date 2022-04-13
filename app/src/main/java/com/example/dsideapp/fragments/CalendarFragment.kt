package com.example.dsideapp.fragments

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class CalendarFragment : Fragment() {
    private lateinit var viewOfLayout: View
    var calendar: CalendarView? = null
    private var dateView: TextView? = null

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_calendar, container, false)
        // Variables for easy manipulation of objects in the activity_main.xml file   🙂
        calendar = viewOfLayout.findViewById<View>(R.id.calendar) as CalendarView
        dateView = viewOfLayout.findViewById<View>(R.id.date_view) as TextView
        //var dayOfWeekView =  viewOfLayout.findViewById<View>(R.id.dayOfWeek) as TextView
        // Listener checks for a tap on a day
        calendar!!
            .setOnDateChangeListener {
                // In here we can probably just make a popup / change page with a view of the day and stuff
                // and query the DB to actually get the event info

                    _, year, month, dayOfMonth->

                // Access and write the rewrite the date ontop of the sreen
                val Date = ("" + (month + 1) + "-"
                        + dayOfMonth.toString() + "-" + year)
                // set this date in TextView for Display
                dateView!!.text = Date

                val weekDay = 1;

                //dayOfWeekView!!.text = weekDay

                // In here we can probably just make a popup / change page with a view of the day and stuff
                // inflate the layout of the popup window
                //val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
//                val popupView: View? =
//                    inflater?.inflate(com.example.dsideapp.R.layout.activity_dailyview, null)
                viewOfLayout = inflater.inflate(R.layout.activity_dailyview, null)
                // create the popup window
                val width = LinearLayout.LayoutParams.FILL_PARENT
                val height = LinearLayout.LayoutParams.FILL_PARENT
                val focusable = true // lets taps outside the popup also dismiss it
                val popupWindow = PopupWindow(viewOfLayout, width, height, focusable)

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken or token idk :)

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 1)
                var windowButton: Button
                lateinit var tempView: View

                //var exitButton = v.findViewById<Button>(R.id.exitPollCreateButton)
                windowButton= viewOfLayout.findViewById<Button>(R.id.lol)

                windowButton.setOnClickListener{
                    Log.w("", "PLEASE WOOOOORK PopUp Window button")
                    popupWindow.dismiss()
                    true
                }
                //dismiss the popup window when touched
                viewOfLayout?.setOnTouchListener { v, event ->
                    popupWindow.dismiss()
                    true
                }

                auth = Firebase.auth
                val database = FirebaseDatabase.getInstance()
                var eventButton: Button
                eventButton = viewOfLayout.findViewById<Button>(R.id.newEvent)
                eventButton.setOnClickListener{
                    // inflate the layout of the popup window
                    viewOfLayout = inflater.inflate(R.layout.fragment_tester_activity, null)
                    Log.w("", "New Event Button is clicked")
                    // create the popup window
                    val width = LinearLayout.LayoutParams.WRAP_CONTENT
                    val height = LinearLayout.LayoutParams.WRAP_CONTENT
                    val focusable = true // lets taps outside the popup also dismiss it
                    val popupWindow = PopupWindow(viewOfLayout, width, height, focusable)

//            //Popup window for the info
//            var infoPopUpText = v.findViewById<TextView>(R.id.popUpTextInfo)
//            val infoDescription = v.findViewById<TextView>(R.id.popUpTextInfo)
//            if (infoDescription != null) {
//                infoPopUpText.text = infoDescription.get(2).text().toString()
//            }
//            var infoImageView = v.findViewById<TextView>(R.id.popUpImageInfo)
//            //textView = findViewById(R.id.title)
//            val infoBitmap
//            infoImageView.setImageBitmap(infoBitmap)

                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window token
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                    viewOfLayout.setOnTouchListener { v, event ->
                        popupWindow.dismiss()
                        true
                    }
                }


            }

        return viewOfLayout
    }
}