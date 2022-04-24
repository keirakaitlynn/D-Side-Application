package com.example.dsideapp.childfragments

import android.annotation.SuppressLint
import android.media.Image
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.data.Effects
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import nl.dionsegijn.konfetti.xml.KonfettiView
import android.view.animation.*
import android.widget.*
import com.example.dsideapp.data.ActivityObject
import com.example.dsideapp.data.LocationObject
import com.example.dsideapp.data.selectedItemsForDecisionTools
import com.example.dsideapp.fragments.selectedActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.ktx.Firebase


class DiceChildFragment : Fragment() {

    private var selectedActivityID: String? = ""
    private var imageViewDice: ImageView? = null
    private val rng = Random()
    private lateinit var viewOfLayout: View
    private lateinit var viewKonfetti: KonfettiView

    //getting database info
    var authorization = auth
    var user = authorization.currentUser
    var userID = authorization.currentUser?.uid
    var db = FirebaseDatabase.getInstance().getReference()

    var randomNum = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var activityList = mutableListOf<String>()
        viewOfLayout = inflater.inflate(R.layout.fragment_child_dice, container, false)
        var activitesOnLeftScreen = ""
        var activitesOnRightScreen = ""
        var numberForActivitesOnLeftScreen = ""
        var numberForActivitesOnRightScreen = ""
        viewKonfetti = viewOfLayout.findViewById(R.id.konfettiView)

        class getDBInfoForDice : AsyncTask<Void, Void, Void>() {

            //Get List of left number
            var leftActivityNums = listOf(viewOfLayout.findViewById<TextView>(R.id.number_of_left_activities1),
                viewOfLayout.findViewById<TextView>(R.id.number_of_left_activities2),
                viewOfLayout.findViewById<TextView>(R.id.number_of_left_activities3),
            )
            //Get List of left activity titles
            var leftActivityTitles = listOf(viewOfLayout.findViewById<TextView>(R.id.left_activities1),
                viewOfLayout.findViewById<TextView>(R.id.left_activities2),
                viewOfLayout.findViewById<TextView>(R.id.left_activities3),
            )

            //Get List of right number
            var rightActivityNums = listOf(viewOfLayout.findViewById<TextView>(R.id.number_of_right_activities1),
                viewOfLayout.findViewById<TextView>(R.id.number_of_right_activities2),
                viewOfLayout.findViewById<TextView>(R.id.number_of_right_activities3),
            )
            //Get List of right activity titles
            var rightActivityTitles = listOf(viewOfLayout.findViewById<TextView>(R.id.right_activities1),
                viewOfLayout.findViewById<TextView>(R.id.right_activities2),
                viewOfLayout.findViewById<TextView>(R.id.right_activities3),
            )

            @SuppressLint("ResourceAsColor")
            override fun doInBackground(vararg params: Void): Void? {
                ///Grabbing all the activities from DB to populate screen
                var leftCounter = 0
                var rightCounter = 0
                //Populate an array to with DB Cart activities
                var activityInfo =
                    db.child("users").child(userID.toString()).get().addOnSuccessListener {
                        if (it.exists()) {
                            val allTheStuff = selectedItemsForDecisionTools
                            allTheStuff.forEach { (key, value) ->
                                var tempName = key.value.toString().substringAfter("title=")
                                    .substringBefore("image_type=").trim()
                                var activitesOnLeftScreen = ""
                                var activitesOnRightScreen = ""
                                //Putting activities on top left or right of the screen
                                if (activityList.size < 3) {
                                    activitesOnLeftScreen += "" + tempName + "\n"
                                    //Adding left activites text
                                    leftActivityTitles[leftCounter].setText(activitesOnLeftScreen)
                                    //Adding circles
                                    leftActivityNums[leftCounter].setText("" + (activityList.size + 1))
                                    leftActivityNums[leftCounter].setBackgroundResource(R.drawable.rounded_corner)
                                    leftActivityNums[leftCounter].setTextColor(R.color.purple_400)
                                    leftCounter++
                                } else if (activityList.size < 6) {
                                    activitesOnRightScreen += "" + tempName + "\n"
                                    //Adding left activites text
                                    rightActivityTitles[rightCounter].setText(activitesOnRightScreen)
                                    //Adding circles
                                    rightActivityNums[rightCounter].setText("" + (activityList.size + 1))
                                    rightActivityNums[rightCounter].setBackgroundResource(R.drawable.rounded_corner)
                                    rightActivityNums[rightCounter].setTextColor(R.color.purple_400)
                                    rightCounter++
                                }

                                //Prevent more than 6 activities fomr being added
                                if (activityList.size < 6) {
                                    activityList.add(tempName)
                                }
                            }
                        }
                        /////
                        //Display activities on screen
                        //Setting the text views with the activites listed
                        //viewOfLayout.findViewById<TextView>(R.id.left_activities_dice).setText(activitesOnLeftScreen)
                        //viewOfLayout.findViewById<TextView>(R.id.right_activities_dice).setText(activitesOnRightScreen)
                        ///
                    }
                var rollButton: Button = viewOfLayout.findViewById(R.id.Button) as Button
                //super.onCreate(savedInstanceState)
                //setContentView(R.layout.fragment_child_dice)
                //imageViewDice = view?.findViewById(R.id.image_view_dice)
                imageViewDice = viewOfLayout.findViewById(R.id.image_view_dice) as ImageView?
                fun rollDice() {
                    randomNum = rng.nextInt(activityList.size)
                    when ( randomNum + 1) {
                        1 -> imageViewDice?.setImageResource(R.drawable.dice1)
                        2 -> imageViewDice?.setImageResource(R.drawable.dice2)
                        3 -> imageViewDice?.setImageResource(R.drawable.dice3)
                        4 -> imageViewDice?.setImageResource(R.drawable.dice4)
                        5 -> imageViewDice?.setImageResource(R.drawable.dice5)
                        6 -> imageViewDice?.setImageResource(R.drawable.dice6)
                    }
                }
                rollButton.setOnClickListener{
                    rollDice()
                    val handler = Handler(context!!.mainLooper)
                    handler.post(Runnable {
                        var tempAct = activityList[randomNum]
                        Toast.makeText(activity, "You've got " + tempAct, Toast.LENGTH_SHORT).show()
                        //Save the overall result
                        var tempDataSnapshot = ""
                            selectedItemsForDecisionTools.forEach { (key, value) ->
                                if (key.value.toString().substringAfter("title=")
                                        .substringBefore("image_type=").trim().contains(tempAct)){
                                    tempDataSnapshot = key.value.toString()
                                    Log.d("selectedActivityID", "${key.key}")
                                    selectedActivityID = key.key
                                }
                        }
                        selectedActivity.id =tempDataSnapshot.substringAfter("key =")
                            .substringBefore(", value =").trim()
                        selectedActivity.business_name = tempDataSnapshot.substringAfter("title=")
                            .substringBefore(", image_type=").trim()
                        selectedActivity.category = tempDataSnapshot.substringAfter("category=")
                            .substringBefore(", title=").trim()
                        selectedActivity.image_type = tempDataSnapshot.substringAfter("image_type=")
                            .substringBefore("} }=").trim()
                        selectedActivity.price = tempDataSnapshot.substringAfter("price=")
                            .substringBefore(", location=").trim()
                        selectedActivity.phone_contact = tempDataSnapshot.substringAfter("phone_contact=")
                            .substringBefore(", price=").trim()
                        explode()
                    })
                }

                var createEventToCalendarButton: Button = viewOfLayout.findViewById(R.id.CreateEvent)
                createEventToCalendarButton.setOnClickListener{
                    if (!selectedActivity.id.isNullOrEmpty()){
                        //Clear the selected from the cart
//                        selectedItemsForDecisionTools.forEach{ (key, value) ->
//                            Log.w("VALUE: ",key.key.toString())
//                            db.child("users").child(userID.toString()).child("data").child("cart")
//                                .child(key.key.toString()).removeValue()
//                        }
                        //Now load the calendar fragment
                        // MMMMM: Load the calendar fragment ------------------------------------------------
                        viewOfLayout = inflater.inflate(com.example.dsideapp.R.layout.fragment_eventadd_pop_up, null)
                        // create the popup window
                        val width = LinearLayout.LayoutParams.MATCH_PARENT
                        val height = LinearLayout.LayoutParams.MATCH_PARENT
                        val focusable = true // lets taps outside the popup also dismiss it
                        val popupWindow = PopupWindow(viewOfLayout, width, height, focusable)
                        // XXXXX -------------------------------------------------------------------------------------------------



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

                        auth = Firebase.auth
                        val database = FirebaseDatabase.getInstance()

                        //Creating the actual event from the button
                        var createEventButton = viewOfLayout.findViewById<Button>(R.id.addEventButton)
                        createEventButton.setOnClickListener() {

                            // MMMMM: Get activity swiped as a DataSnapshot, "cartActivityToAddToCalendarTEMP"
                            val cartActivityToAddToCalendarID = selectedActivityID
                            var cartActivityToAddToCalendarTEMP : DataSnapshot? = null // initialize if !it.exists()
                            var cartActivityInfo = db.child("users").child(userID.toString()).get().addOnSuccessListener {
                                if (it.exists()){ // XXXXX: ------------------------------
                                    // NOTES: allTheStuff = array of Activities in Cart
                                    cartActivityToAddToCalendarTEMP =
                                        cartActivityToAddToCalendarID?.let { it1 ->
                                            it.child("data").child("cart").child(
                                                it1
                                            )
                                        }
                                    Log.d("ADDING","${cartActivityToAddToCalendarTEMP}")

                                    //Creating vars to gather user input for event info
                                    //var eventTitle = viewOfLayout.findViewById<TextView>(R.id.eventName).text.toString()
                                    //var eventDate = viewOfLayout.findViewById<DatePicker>(R.id.datePicker)
                                    val day = datePicker.dayOfMonth
                                    val month = datePicker.month
                                    val year = datePicker.year

                                    var eventTime = viewOfLayout.findViewById<TextView>(R.id.TimeText)

                                    // MMMMM: Convert Date & Time to Date Class.
                                    var date = Date(year, month, day)

                                    // MMMMM: Add info to create Event Object.
                                    //Getting db info
                                    var authorization = auth
                                    var user = authorization.currentUser
                                    var userID = authorization.currentUser?.uid
                                    var db = FirebaseDatabase.getInstance().getReference("users").child(userID.toString())

                                    //Creating the random event ID
                                    var i = 0
                                    var eventId = ""
                                    for (i in 1..5) {
                                        eventId += kotlin.random.Random.nextInt(9)
                                    }
                                    for (i in 1..5) {
                                        eventId += (kotlin.random.Random.nextInt(25) + 65).toChar()
                                    }

                                    //////In here will be on button click of recycler view, friends are added to a mutable list and added to db
                                    var friendsInvited = mutableListOf<String>()
                                    var friendDBList = ""
                                    //hardcoding an added friend for testing purposes
                                    friendsInvited.add("WHBqJbAom0Yz0MQPQg0zuDnv4Xv1")
                                    friendDBList += "WHBqJbAom0Yz0MQPQg0zuDnv4Xv1;"

                                    // MMMMM: 1. Convert DataSnapshot to Activity.
                                    //Log.d("KEY:TEMP", "${cartActivityToAddToCalendarTEMP.key}")
                                    //Log.d("ID:TEMP", "${cartActivityToAddToCalendarTEMP.child("id").value}")
                                    //Creating writeToDB function
                                    fun dataSnapshotToActivityToEventToDB(cartActivity: DataSnapshot, userId: String, id: String, title: String = "None", phone: String = "None",
                                                                          image: String = "None", loc_address: String = "None", loc_city: String = "None",
                                                                          loc_country:String = "None", loc_zip: String = "None", loc_state: String = "None"
                                                                          , business_name: String = "None", price: String = "None", category: String = "None", event_id : String = "None", event_title : String = "None", date : Date? = null, users_invited: MutableList<String>? = null) {
                                        val location = LocationObject(loc_address, loc_city, loc_country, loc_zip, loc_state)
                                        val activity = ActivityObject(if(id != "") id else "null", title, phone, image, location, business_name, price, category)
                                        Log.d("EVENT TITLE", "${title}") // XXXXX: ------------------------------
                                        val event = users_invited?.let { it1 ->
                                            activity.toEvent(title, date, date,
                                                it1
                                            )
                                        }
                                        Log.d("EVENT", "${event}")

                                        //Setting the event in the db
                                        //db.child("data").child("events").child(eventId).setValue(event)
                                        //db.child("data").child("events").child(eventId).setValue(title)
                                        //                    //Creating a db readable event
                                        data class stringEvent(
                                            val id: String? = null,
                                            val event_title: String? = null,
                                            val start_time: String? = null,
                                            val end_time: String? = null,
                                            val activity: ActivityObject? = null,
                                            val users: String? = null,
                                        ) {}
                                        if (date != null) {
                                            db.child("data").child("events").child(eventId).setValue(stringEvent(event_id, title, date.toString(), date.toString(), activity, users_invited.toString()))
                                        }

                                        // MMMMM: 2. LEFT Swipe functionality
                                        // NNNNN: 1. delete cartActivity from DATABASE
                                        val cartActivityToDelete =
                                            activity.id?.let { it1 ->
                                                it.child("data").child("cart").child(
                                                    it1
                                                )
                                            }
                                        if (cartActivityToDelete != null) {
                                            cartActivityToDelete.getRef().removeValue()
                                        }
                                        // NNNNN: 2. delete cartActivity from VIEW AFTER deleting cartActivity from DATABASE (bc of viewHolder.position)
                                        //(adapter as CartActivityAdapter).deleteItem(cartActivity)
                                        //(adapter as CartActivityAdapter).notifyDataSetChanged()

                                    }
                                    Log.d("TOEVENT", "{$cartActivityToAddToCalendarTEMP}")
                                    cartActivityToAddToCalendarTEMP?.child("id")?.value
                                    cartActivityToAddToCalendarTEMP?.let { it1 ->
                                        dataSnapshotToActivityToEventToDB(
                                            it1, userId = userID.toString(), id = cartActivityToAddToCalendarTEMP!!.child("id").value.toString(),
                                            title = cartActivityToAddToCalendarTEMP!!.child("title").value.toString(), phone = cartActivityToAddToCalendarTEMP!!.child("phone_contact").value.toString(), image = cartActivityToAddToCalendarTEMP!!.child("image_type").value.toString(),
                                            business_name = cartActivityToAddToCalendarTEMP!!.child("business_name").value.toString(), price = cartActivityToAddToCalendarTEMP!!.child("price").value.toString(), category = cartActivityToAddToCalendarTEMP!!.child("category").value.toString(),
                                            event_id = eventId, event_title = cartActivityToAddToCalendarTEMP!!.child("title").value.toString(), date = date, users_invited = friendsInvited)
                                    }

                                    // XXXXX -----------------------------------------------------
                                    //Log.d("Item Swiped RIGHT", "${viewHolder.position} Activity")
                                    // XXXXX -----------------------------------------------------
                                }
                                // NOTES: Update RecyclerAdapter with changes.
                                //adapter?.notifyDataSetChanged()
                            }

                            Log.d("AFTERIT", "{$cartActivityToAddToCalendarTEMP}")
                        }



                        // XXXXX -------------------------------------------------------------------------------------------------
                        // show the popup window
                        // which view you pass in doesn't matter, it is only used for the window token
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                        viewOfLayout.setOnTouchListener { v, event ->
                            popupWindow.dismiss()
                            true
                        }
                    }
                    else{
                        Toast.makeText(activity, "An activity hasn't been chosen yet!", Toast.LENGTH_SHORT).show()
                    }
                }

                return null
            }
        }
        getDBInfoForDice().execute()

        return viewOfLayout
    }
    private fun explode() {
        viewKonfetti.start(Effects.explode())
    }
}