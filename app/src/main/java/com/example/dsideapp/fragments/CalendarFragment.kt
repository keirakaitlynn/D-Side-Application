/*

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
                                    var userdb = FirebaseDatabase.getInstance().getReference("users")
                                    //THIS CODE LINE IS NOT TESTED. IT WAS ADDED WHILE THE ENTIRE FUNCTION WAS COMMENTED OUT :(
                                    val curr_favorites = userdb.child(userID).child("favorite_Activities").get().toString()
                                    if(curr_favorites == null){

                                    }
                                    userdb.child(userID).child("favorite_Activities").setValue( + "," + eventItr.activity.category)
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

//Josh's Version to do add event to db and send event info to other users

package com.example.dsideapp.fragments

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
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.data.EventObject
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.random.Random
import android.widget.*
import com.example.dsideapp.auth
import com.example.dsideapp.data.ActivityObject
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.io.IOException


class CalendarFragment : Fragment() {
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
        viewOfLayout = inflater.inflate(R.layout.fragment_calendar, container, false)

        //PUTTING IN FEEDBACK PLS WORK :(
        //PUTTING IN FEEDBACK PLS WORK :(

        auth = Firebase.auth
        val database = FirebaseDatabase.getInstance()

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

                    var last_checked = Long.MIN_VALUE
                    database.reference.child("users").child(auth.uid.toString()).child("last_checked").get().addOnSuccessListener {
                        Log.i("firebase", "Got value ${it.value}")
                        last_checked = it.value as Long
                        if(events.size != 0) {
                            for (eventItr in events) {
                                //IF EVENT PASSED AND HASN'T BEEN CHECKED
                                if (Date().time > (eventItr.end_time!!).time && (eventItr.end_time!!).time  > last_checked) {
                                    var waiting = true
                                    //SHOW POPUP
                                    // inflate the layout of the popup window
                                    viewOfLayout = inflater.inflate(com.example.dsideapp.R.layout.fragment_feedback_pop_up, null)
                                    // create the popup window
                                    val width = LinearLayout.LayoutParams.WRAP_CONTENT
                                    val height = LinearLayout.LayoutParams.WRAP_CONTENT
                                    val focusable = false // lets taps outside the popup also dismiss it
                                    val popupWindow = PopupWindow(viewOfLayout, width, height, focusable)

                                    //Popup window for the event name and buttons
                                    popUpEventText = viewOfLayout.findViewById(R.id.popUpEventName)
                                    popUpEventLike = viewOfLayout.findViewById(R.id.like_button)
                                    popUpEventDislike = viewOfLayout.findViewById(R.id.dislike_button)

                                    popUpEventText.text = eventItr.event_title

                                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                                    //Set up text and setOnClickListeners
                                    popUpEventText.text = eventItr.event_title
                                    popUpEventLike.setOnClickListener {
                                        //Add eventItr's activity category to user favorites.
                                        var userdb = FirebaseDatabase.getInstance().getReference("users")
                                        //THIS CODE LINE IS NOT TESTED. IT WAS ADDED WHILE THE ENTIRE FUNCTION WAS COMMENTED OUT :(
                                        userdb.child(auth.uid.toString()).child("favorite_Activities").get().addOnSuccessListener {
                                            var curr_favorites = HashSet(it.value.toString().split(","))
                                            if(curr_favorites.first().equals("")){
                                                curr_favorites.remove("")
                                            }
                                            curr_favorites.add(eventItr.activity!!.category.toString())
                                            Log.w("help", curr_favorites.toString())
                                            userdb.child(auth.uid.toString()).child("favorite_Activities").setValue(curr_favorites.toString().replace("[","").replace("]",""))
                                            popupWindow.dismiss()
                                        }.addOnFailureListener{
                                            Log.e("firebase", "Error getting data", it)
                                        }
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
                        database.reference.child("users").child(auth.uid.toString()).child("last_checked").setValue(Date().getTime())
                    }.addOnFailureListener{
                        Log.e("firebase", "Error getting data", it)
                        database.reference.child("users").child(auth.uid.toString()).child("last_checked").setValue(Date().getTime())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    throw databaseError.toException()
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }


        //Popup Code

        //GET ALL EVENTS IN USER DB AND PUTS IT IN EVENTS ARRAYLIST
        //ITERATE THROUGH EVENTS IF SIZE != 0

        //PUTTING IN FEEDBACK PLS WORK :(
        //PUTTING IN FEEDBACK PLS WORK :(

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

                    var eventDate_split = eventDate.text.toString().split("/")
                    var startTime = Date(eventDate_split[2].toInt(), eventDate_split[1].toInt(), eventDate_split[0].toInt())
                    //Creating the event in the db, leving friends empty
                    var dbReadableEvent = EventObject(eventId, eventTitle.text.toString(), startTime,
                        Date(startTime.time + (eventTime.text.toString().toInt() * 60 * 1000)), ActivityObject() , user?.email.toString(),null)
                    //Setting the event in the db
                    db.child("data").child("events").child(eventId).setValue(dbReadableEvent)

                    //////In here will be on button click of recycler view, friends are added to a mutable list and added to db
                    var friendsInvited = mutableListOf<String>()
                    var friendDBList = ""
                    //hardcoding an added friend for testing purposes
                    friendsInvited.add("WHBqJbAom0Yz0MQPQg0zuDnv4Xv1")
                    friendDBList += "WHBqJbAom0Yz0MQPQg0zuDnv4Xv1;"
                    db.child("data").child("events").child(eventId).child("users").setValue(friendDBList)
                    //Updating the event invite list
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
