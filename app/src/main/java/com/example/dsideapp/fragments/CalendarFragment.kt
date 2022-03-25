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