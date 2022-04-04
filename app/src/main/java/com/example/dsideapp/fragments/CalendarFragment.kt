/*
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
*/

//Josh's Version to do add event to db and send event info to other users
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
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random


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
////                val popupView: View? =
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

                //var exitButton = v.findViewById<Button>(R.id.exitPollCreateButton)//
                windowButton= viewOfLayout.findViewById<Button>(R.id.lol)

                windowButton.setOnClickListener{
                    Log.w("", "PLEASE WOOOOORK PopUp Window button")
                    popupWindow.dismiss()
                    true
                }

                //Creating the actual event from the button
                var createEventButton = viewOfLayout.findViewById<Button>(R.id.newEvent)
                createEventButton.setOnClickListener(){
                    //Creating vars to gather user input for event info
                    var eventTitle = viewOfLayout.findViewById<TextView>(R.id.ActivityTitleText)
                    var eventDate = viewOfLayout.findViewById<TextView>(R.id.DateText)
                    var eventTime = viewOfLayout.findViewById<TextView>(R.id.TimeText)

                    //Creating a db readable event
                    data class stringEvent(
                        val event_Id: String? = null,
                        val event_Title: String? = null,
                        val event_Date: String? = null,
                        val event_Time: String? = null,
                        val event_Poster: String? = null,
                        val event_InviteList: String? = null,
                    ) {}

                    //Getting db info
                    var authorization = auth
                    var user = authorization.currentUser
                    var userID = authorization.currentUser?.uid
                    var db = FirebaseDatabase.getInstance().getReference("users").child(userID.toString())

                    //Creating the random event ID
                    var i = 0
                    var eventId = ""
                    for(i in 1..5){
                         eventId += Random.nextInt(9)
                    }
                    for(i in 1..5){
                         eventId += (Random.nextInt(25) + 65).toChar()
                    }

                    //Creating the event in the db, leving friends empty
                    var dbReadableEvent = stringEvent(eventId, eventTitle.text.toString(), eventDate.text.toString(),
                        eventTime.text.toString(), user?.email.toString(),"None")
                    //Setting the event in the db
                    db.child("data").child("events").child(eventId).setValue(dbReadableEvent)

                    //////In here will be on button click of recycler view, friends are added to a mutable list and added to db
                    var friendsInvited = mutableListOf<String>()
                    var friendDBList = ""
                    //hardcoding an added friend for testing purposes
                    friendsInvited.add("WHBqJbAom0Yz0MQPQg0zuDnv4Xv1")
                    friendDBList += "WHBqJbAom0Yz0MQPQg0zuDnv4Xv1;"
                    db.child("data").child("events").child(eventId).child("event_InviteList")
                        .setValue(friendDBList)
                    //Updating the event invite list
                    dbReadableEvent = stringEvent(eventId, eventTitle.text.toString(), eventDate.text.toString(),
                        eventTime.text.toString(), user?.email.toString(),friendDBList)
                    //db ref to write event info into friend's events
                    var friendDB = FirebaseDatabase.getInstance().getReference("users")

                    //Setting event's friend list
                    friendsInvited.forEach{friend ->
                        //putting the event in friend's events
                        friendDB.child(friend).child("data").child("events").child(eventId).setValue(dbReadableEvent)
                    }
                    //////



                    //Just adding friends to db so that child path exists
                    //var friend_db = FirebaseDatabase.getInstance().getReference("users")
                   // db.child("friends").child("WHBqJbAom0Yz0MQPQg0zuDnv4Xv1").
                    //    setValue("WHBqJbAom0Yz0MQPQg0zuDnv4Xv1")
                   // db.child("friends").child("LKal75HYTcSwFwri88v6OQNSGkt2").
                   //     setValue("LKal75HYTcSwFwri88v6OQNSGkt2")
                  //  db.child("friends").child("IaV5KRr1XEXqvVHENqI4ukP4qVm2").
                  //      setValue("IaV5KRr1XEXqvVHENqI4ukP4qVm2")

                }
                //dismiss the popup window when touched
               // viewOfLayout?.setOnTouchListener { v, event ->
               //     popupWindow.dismiss()
               //     true
               // }
            }

        return viewOfLayout
    }
}
