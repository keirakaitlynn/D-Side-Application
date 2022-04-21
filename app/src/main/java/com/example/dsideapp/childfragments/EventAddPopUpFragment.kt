package com.example.dsideapp.childfragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random


class EventAddPopUpFragment : Fragment() {

    private lateinit var viewOfLayout: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater.inflate(R.layout.fragment_eventadd_pop_up, container, false)


        val datePicker = viewOfLayout.findViewById<DatePicker>(R.id.datePicker)
        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        )
        {
                view, year, month, day ->
            val month = month + 1
            //val msg = "You Selected: $day/$month/$year"
            //Log.w("", msg)
        }

        datePicker!!.setOnDateChangedListener {
                view, year, month, dayOfMoth->
            val month = month + 1


//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Toast.makeText(app.com.sample.MainActivity.this," You are changed date is : "+dayOfMonth +" - "+monthOfYear+ " - "+year,Toast.LENGTH_LONG).show();
            }



        auth = Firebase.auth
       // val database = FirebaseDatabase.getInstance()

        //Creating the actual event from the button
        var createEventButton = viewOfLayout.findViewById<Button>(R.id.addEventButton)

        //Creating vars to gather user input for event info
        var eventTitle = viewOfLayout.findViewById<EditText>(R.id.eventName)
        //var eventDate = viewOfLayout.findViewById<DatePicker>(R.id.datePicker)
        val eventDay = datePicker.dayOfMonth.toString()
        val eventMonth = datePicker.month.toString()
        val eventYear = datePicker.year.toString()
        var eventDate: String = eventDay + eventMonth + eventYear

        var eventTime = viewOfLayout.findViewById<EditText>(R.id.TimeText)
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


        createEventButton.setOnClickListener() {


            //Creating the random event ID
            var i = 0
            var eventId = ""
            for (i in 1..5) {
                eventId += Random.nextInt(9)
            }
            for (i in 1..5) {
                eventId += (Random.nextInt(25) + 65).toChar()
            }

            //Creating the event in the db, leaving friends empty

            var dbReadableEvent = StringEvent(
                eventId, eventTitle.text.toString(), eventDate.toString(),
                eventTime.text.toString(), user?.email.toString(), "None"
            )
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


        }
            return viewOfLayout
        }

}

