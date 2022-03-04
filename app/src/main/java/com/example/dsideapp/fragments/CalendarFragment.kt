package com.example.dsideapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.data.ActivityObject
import com.example.dsideapp.data.EventObject
import com.example.dsideapp.data.LocationObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var viewOfLayout: View
    var calendar: CalendarView? = null
    private var dateView: TextView? = null

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_calendar, container, false)
        //
        auth = Firebase.auth
        val database = FirebaseDatabase.getInstance()
        val start_time = Calendar.getInstance()
        val end_time = Calendar.getInstance()
        end_time.add(Calendar.MINUTE, 90)
        val e_activity = ActivityObject("050FCV","Coffee MCO","None","\"https://s3-media2.fl.yelpcdn.com/bphoto/w_MuU2gYysZXSoFb14mtJA/o.jpg\"", LocationObject(), "None", "$")
        val user_list = MutableList(3) { index -> "A" + index }
        val e_event = EventObject("Coffee with frens",start_time, end_time, e_activity, user_list, )
        database.reference.child("users").child(auth.uid.toString()).child("data").child("calendar").child(if(e_activity.id!! != "") e_activity.id!! else "null").setValue(e_event)

        // Variables for easy manipulation of objects in the activity_main.xml file   🙂
        calendar = viewOfLayout.findViewById<View>(R.id.calendar) as CalendarView
        dateView = viewOfLayout.findViewById<View>(R.id.date_view) as TextView

        // Listener checks for a tap on a day
        calendar!!
            .setOnDateChangeListener {
                // In here we can probably just make a popup / change page with a view of the day and stuff
                // and query the DB to actually get the event info 🙂

                _, year, month, dayOfMonth ->

                // Access and write the rewrite the date ontop of the sreen
                val Date = ("" + (month + 1) + "-"
                        + dayOfMonth.toString() + "-" + year)
                // set this date in TextView for Display
                dateView!!.text = Date
            }

        return viewOfLayout
    }
}