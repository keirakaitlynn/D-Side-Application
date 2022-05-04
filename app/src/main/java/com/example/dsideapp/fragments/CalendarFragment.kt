/*
package com.example.dsideapp.fragments

import android.annotation.SuppressLint
import android.location.Location
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
import java.security.Timestamp
import java.util.*
import kotlin.collections.ArrayList
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

        /*
        WRITE EVENTS WITH EMPTY ACTIVITIES
         */
        //Write to database using
        var i = 0
        var ArandID = ""
        for(i in 1..3){
            ArandID += Random.nextInt(9)
        }
        for(i in 1..3){
            ArandID += (Random.nextInt(25) + 65).toChar()
        }
        var j = 0
        var ErandID = ""
        for(j in 1..3){
            ErandID += Random.nextInt(9)
        }
        for(j in 1..3){
            ErandID += (Random.nextInt(25) + 65).toChar()
        }
        val eventtowrite = EventObject(ErandID,"TestEvent", Date(Date().getTime()) , Date(Date().getTime()), ActivityObject(ArandID,"","","",LocationObject(),"",""), null)

        database.reference.child("users").child(auth.uid.toString()).child("data").child("events").child(ErandID).setValue(eventtowrite)

        var last_checked = Long.MIN_VALUE
        database.reference.child("users").child(auth.uid.toString()).child("last_checked").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            last_checked = it.value as Long
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
        val events = arrayListOf<EventObject>()
        try {
            var ref = database.reference.child("users").child(auth.uid.toString()).child("data")
                .child("events")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (productSnapshot in dataSnapshot.getChildren()) {
                        val readItem = productSnapshot.getValue(EventObject::class.java)
                        events.add(readItem!!)
                    }
                    if(events.size != 0) {
                        for (eventItr in events) {
                            //IF EVENT PASSED AND HASN'T BEEN CHECKED
                            if (Date().getTime() > (eventItr.end_time!!).time && (eventItr.end_time!!).time  > last_checked) {
                                var waiting = true
                                Log.w("data", "Event has passed")
                                //SHOW POPUP
                                // inflate the layout of the popup window
                                v = inflater.inflate(com.example.dsideapp.R.layout.fragment_feedback_pop_up, null)
                                // create the popup window
                                val width = LinearLayout.LayoutParams.WRAP_CONTENT
                                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                                val focusable = false // lets taps outside the popup also dismiss it
                                val popupWindow = PopupWindow(v, width, height, focusable)

                                //Popup window for the event name and buttons
                                popUpEventText = v.findViewById(R.id.popUpEventName)
                                popUpEventLike = v.findViewById(R.id.like_button)
                                popUpEventDislike = v.findViewById(R.id.dislike_button)

                                popUpEventText.text = eventItr.event_title

                                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                                Log.w("data", "Popup Shown")
                                //Set up text and setOnClickListeners
                                popUpEventText.text = eventItr.event_title
                                popUpEventLike.setOnClickListener {
                                    //Add eventItr's activity category to user favorites.
                                    popupWindow.dismiss()
                                }
                                popUpEventDislike.setOnClickListener {
                                    //Do nothing probably?
                                    popupWindow.dismiss()
                                }
                                // show the popup window
                                // which view you pass in doesn't matter, it is only used for the window token
                            }
                        }
                    }
                    else {
                        Log.w("data", "Events are empty")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    throw databaseError.toException()
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }

        database.reference.child("users").child(auth.uid.toString()).child("last_checked").setValue(Date().getTime())

        //Popup Code

        //GET ALL EVENTS IN USER DB AND PUTS IT IN EVENTS ARRAYLIST
       //ITERATE THROUGH EVENTS IF SIZE != 0




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
import java.util.*
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



        auth = Firebase.auth
        // val database = FirebaseDatabase.getInstance()

        //Creating the actual event from the button
        //var createEventButton = viewOfLayout.findViewById<Button>(R.id.addEventButton)


        //
        //Creating a db readable event
        data class StringEvent(
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


//
//        val eventTime1 = viewOfLayout.findViewById<TextView>(R.id.eventTimeText)
//        val eventDate1 = viewOfLayout.findViewById<TextView>(R.id.eventDateText)
//        val eventTitle1 = viewOfLayout.findViewById<TextView>(R.id.eventNameText)



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


                viewOfLayout = inflater.inflate(R.layout.activity_dailyview, null)
                // create the popup window
                var width = LinearLayout.LayoutParams.FILL_PARENT
                var height = LinearLayout.LayoutParams.FILL_PARENT
                var focusable = true // lets taps outside the popup also dismiss it
                var popupWindow = PopupWindow(viewOfLayout, width, height, focusable)

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken or token idk :)

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 1)
                lateinit var tempView: View


                var windowButton: Button = viewOfLayout.findViewById<Button>(R.id.exitButton)

                windowButton.setOnClickListener{
                    Log.w("", "PLEASE WOOOOORK PopUp Window button")
                    popupWindow.dismiss()
                    true
                }

                //----------------NEW EVENT BUTTON----------------------

                var newEventButton: Button = viewOfLayout.findViewById<Button>(R.id.addEventButton)

                newEventButton.setOnClickListener {



                    val datePicker = viewOfLayout.findViewById<DatePicker>(R.id.datePicker)
                    val today = Calendar.getInstance()

                    datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                        today.get(Calendar.DAY_OF_MONTH)

                    )
                    {
                            view, year, month, day ->
                        val month = month + 1
                        val msg = "You Selected: $day/$month/$year"
                        Log.w("", msg)
                    }


                    //Creating the random event ID
                    var i = 0
                    var eventId = ""
                    for (i in 1..5) {
                        eventId += Random.nextInt(9)
                    }
                    for (i in 1..5) {
                        eventId += (Random.nextInt(25) + 65).toChar()
                    }
                    //Creating vars to gather user input for event info
                    var eventTitle = viewOfLayout.findViewById<EditText>(R.id.eventName)
                    //var eventDate = viewOfLayout.findViewById<DatePicker>(R.id.datePicker)
                    val eventDay = datePicker.dayOfMonth.toString()
                    val eventMonth = datePicker.month.toString()
                    val eventYear = datePicker.year.toString()
                    //var eventDate: String = eventDay + eventMonth + eventYear
                    var eventDate = eventMonth + " " + eventDay +", " + eventYear

                    var eventTime = viewOfLayout.findViewById<EditText>(R.id.TimeText)



                    //Creating the event in the db, leaving friends empty

                    var dbReadableEvent = StringEvent(
                        eventId, eventTitle.text.toString(), eventDate.toString(),
                        eventTime.text.toString(), user?.email.toString(), "None"
                    )
                    //Setting the event in the db
                    //Log.w("", "JOshhhhhhhhhhhhhhhh")
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
                    dbReadableEvent = StringEvent(
                        eventId, eventTitle.text.toString(), eventDate.toString(),
                        eventTime.text.toString(), user?.email.toString(), friendDBList
                    )
                    //db ref to write event info into friend's events
                    var friendDB = FirebaseDatabase.getInstance().getReference("users")

                    //Setting event's friend list
                    friendsInvited.forEach { friend ->
                        //putting the event in friend's events
                        friendDB.child(friend).child("data").child("events").child(eventId)
                            .setValue(dbReadableEvent)
                    }

                    //assign each textview to a variable to update the UI with the added event information
                    val eventTime1 = viewOfLayout.findViewById<TextView>(R.id.eventTimeText)
                    val eventDate1 = viewOfLayout.findViewById<TextView>(R.id.eventDateText)
                    val eventTitle1 = viewOfLayout.findViewById<TextView>(R.id.eventNameText)


                    val eventTimeString = eventTime.text.toString()
                    val eventDateString = eventDate.toString()
                    val eventTitleString = eventTitle.text.toString()

                    eventTime1.setText(eventTimeString)
                    eventDate1.setText(eventDateString)
                    eventTitle1.setText(eventTitleString)


                    var deleteEventButton = viewOfLayout.findViewById<Button>(R.id.DeleteEventButton)

                    deleteEventButton.setOnClickListener {

                        Log.w("", "JOshhhhhhhhhhhhhhhh")

                        db.child("data").child("events").child(eventId).removeValue()


                        eventTime1.setText(" ")
                        eventDate1.setText(" ")
                        eventTitle1.setText(" ")

                    }


                //after creating an event, the daily view will load the added event

                var updateEventButton: Button

                updateEventButton= viewOfLayout.findViewById<Button>(R.id.updateEventButton)

                updateEventButton.setOnClickListener {
                    db.child("data").child("events").child(eventId)
                        .setValue(viewOfLayout.findViewById<EditText>(R.id.eventName).text.toString())


                    db.child("data").child("events").child(eventId)
                        .setValue(eventDate).toString()

                    db.child("data").child("events").child(eventId)
                        .setValue(viewOfLayout.findViewById<EditText>(R.id.TimeText).text.toString())

                    val eventTime1 = viewOfLayout.findViewById<TextView>(R.id.eventTimeText)
                    val eventDate1 = viewOfLayout.findViewById<TextView>(R.id.eventDateText)
                    val eventTitle1 = viewOfLayout.findViewById<TextView>(R.id.eventNameText)


                    val eventTimeString = eventTime.text.toString()
                    val eventDateString = eventDate.toString()
                    val eventTitleString = eventTitle.text.toString()

                    eventTime1.setText(eventTimeString)
                    eventDate1.setText(eventDateString)
                    eventTitle1.setText(eventTitleString)

                }



                }




            }


//
//        val eventTimeString = eventTime.text.toString()
//        val eventDateString = eventDate.toString()
//        val eventTitleString = eventTitle.text.toString()
//
//        eventTime1.setText(eventTimeString)
//        eventDate1.setText(eventDateString)
//        eventTitle1.setText(eventTitleString)


        return viewOfLayout
    }
}

