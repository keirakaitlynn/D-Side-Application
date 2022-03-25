package com.example.dsideapp.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.data.ActivityObject
import com.example.dsideapp.data.EventObject
import com.example.dsideapp.data.LocationObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.util.*
import kotlin.random.Random

class CalendarFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var viewOfLayout: View
    var calendar: CalendarView? = null
    private var dateView: TextView? = null
    //Popup Variables
    private lateinit var popUpEventText: TextView
    private lateinit var popUpEventLike: ImageButton
    private lateinit var popUpEventDislike: ImageButton


    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var v  = inflater.inflate(R.layout.fragment_calendar, container, false)

        auth = Firebase.auth
        val database = FirebaseDatabase.getInstance()

        class ActivityDBReader : AsyncTask<Void , Void, ArrayList<ActivityObject>>() {

            @SuppressLint("ClickableViewAccessibility")
            override fun doInBackground(vararg params: Void): ArrayList<ActivityObject>? {
                try {
                    //Read all activites
                    val readables = arrayListOf<ActivityObject>()
                    var ref = database.reference.child("users").child(auth.uid.toString()).child("data").child("cart")
                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            for (productSnapshot in dataSnapshot.getChildren()) {
                                val readItem = productSnapshot.getValue(ActivityObject::class.java)
                                Log.w("data", readItem!!.id!!)
                                readables.add(readItem)
                            }
                            for (readerItr in readables) {
                                System.out.println(readerItr)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            throw databaseError.toException()
                        }
                    })
                    return readables
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return null
            }
        }
        class EventDBReader : AsyncTask<Void , Void, ArrayList<EventObject>>() {

            @SuppressLint("ClickableViewAccessibility")
            override fun doInBackground(vararg params: Void): ArrayList<EventObject>? {
                try {
                    //Read all activites
                    val readables = arrayListOf<EventObject>()
                    var ref = database.reference.child("users").child(auth.uid.toString()).child("data").child("events")
                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            for (productSnapshot in dataSnapshot.getChildren()) {
                                val readItem = productSnapshot.getValue(EventObject::class.java)
                                Log.w("data", readItem!!.id!!)
                                readables.add(readItem)
                            }
                            for (readerItr in readables) {
                                System.out.println(readerItr)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            throw databaseError.toException()
                        }
                    })
                    return readables
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return null
            }
        }
        //Write to database using
        var i = 0
        var randID = ""
        for(i in 1..3){
            randID += Random.nextInt(9)
        }
        for(i in 1..3){
            randID += (Random.nextInt(25) + 65).toChar()
        }
        var activities = arrayListOf<ActivityObject>()
        activities.addAll(ActivityDBReader().execute().get())
        val events = arrayListOf<EventObject>()
        events.addAll(EventDBReader().execute().get())
        val eventtowrite = EventObject(randID,"TestEvent", Calendar.getInstance(), Calendar.getInstance(), activities.first(), null, false)

        database.reference.child("users").child(auth.uid.toString()).child("data").child("events").child(i.toString()).setValue(eventtowrite)

        //Popup Code

        //GET ALL EVENTS IN USER DB AND PUTS IT IN EVENTS ARRAYLIST
               //ITERATE THROUGH EVENTS IF SIZE != 0
                if(events.size != 0) {
                    for (eventItr in events) {
                        //IF EVENT PASSED AND HASN'T BEEN CHECKED
                        if (Calendar.getInstance() > eventItr.end_time!! && !eventItr.checked) {
                            //SHOW POPUP
                            // inflate the layout of the popup window
                            v = inflater.inflate(com.example.dsideapp.R.layout.fragment_feedback_pop_up, null)
                            // create the popup window
                            val width = LinearLayout.LayoutParams.WRAP_CONTENT
                            val height = LinearLayout.LayoutParams.WRAP_CONTENT
                            val focusable = false // lets taps outside the popup also dismiss it
                            val popupWindow = PopupWindow(v, width, height, focusable)

                            //Popup window for the event name and buttons
                            popUpEventText = v.findViewById(R.id.popUpImageInfo)
                            popUpEventLike = v.findViewById(R.id.like_button)
                            popUpEventDislike = v.findViewById(R.id.dislike_button)

                            //Set up text and setOnClickListeners
                            popUpEventText.text = eventItr.event_title
                            popUpEventLike.setOnClickListener {
                                //Add eventItr's activity category to user favorites.
                                eventItr.checked = true
                                popupWindow.dismiss()
                            }
                            popUpEventDislike.setOnClickListener {
                                //Do nothing probably?
                                eventItr.checked = true
                                popupWindow.dismiss()
                            }
                            // show the popup window
                            // which view you pass in doesn't matter, it is only used for the window token
                            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                            v.setOnTouchListener { v, event ->
                                popupWindow.dismiss()
                                true
                            }
                        }
                    }
                }



        // Variables for easy manipulation of objects in the activity_main.xml file   🙂
        calendar = v.findViewById<View>(R.id.calendar) as CalendarView
        dateView = v.findViewById<View>(R.id.date_view) as TextView

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

        return v
    }
}