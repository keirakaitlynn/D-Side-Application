package com.example.dsideapp.childfragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random
import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.dsideapp.R.color.*
import com.example.dsideapp.data.ActivityObject
import com.example.dsideapp.data.Effects
import com.example.dsideapp.data.LocationObject
import nl.dionsegijn.konfetti.xml.KonfettiView
import com.example.dsideapp.data.selectedItemsForDecisionTools
import com.example.dsideapp.fragments.HomeFragment
import com.example.dsideapp.fragments.selectedActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.ktx.Firebase
import java.util.*
import com.example.dsideapp.fragments.ppw

var ppwWheel = PopupWindow()

class WheelChildFragment : Fragment() {

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val v = inflater.inflate(R.layout.fragment_child_wheel, container, false)
//        return v
//    }
    private var pleaseWorkManager : FragmentManager? = null

    private var selectedActivityID: String? = ""

    private lateinit var viewOfLayout: View

    // Roughly how big each user value's is represented on the wheel.
    private lateinit var sectorDegrees: IntArray
    var calendar: CalendarView? = null
    private var dateView: TextView? = null
    private lateinit var viewKonfetti: KonfettiView

    //getting database info
    var authorization = auth
    var user = authorization.currentUser
    var userID = authorization.currentUser?.uid
    var db = FirebaseDatabase.getInstance().getReference()

    // Offset to make sure the result aligns with the little green triangle
    //  Index 0 will never be used since there will always be 1+ things to spin.
    //  Needed for other calculations tho
    private val rotationOffsetForResult = arrayOf(0F,-50F, -40F, -30F, -20F, -20F, -20F, -30F, -30F, -20F)
    // Degree that represents the location of the random result
    private var degree : Int = 0
    var isSpinning : Boolean = false
    // Wheel image taken from the XML
    private var wheel: ImageView? = null
    //Tempt list of activities

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_child_wheel, container, false)
        viewKonfetti = viewOfLayout.findViewById(R.id.konfettiView)

        class getDBInfoForWheel : AsyncTask<Void, Void, Void>() {
            // array of the user's values. RN its just #'s but who cares
            var sectors = ""
            // size of sector
            var sectorsSize = 0
            // list of all the activities
            var activityList = mutableListOf<String>()
            @SuppressLint("ResourceAsColor")
            override fun doInBackground(vararg params: Void): Void? {
                ///Grabbing all the activities from DB to populate screen
                //Populate an array to with DB Cart activities

                //Get List of left number
                var leftActivityNums = listOf(viewOfLayout.findViewById<TextView>(R.id.number_of_left_activities1),
                    viewOfLayout.findViewById<TextView>(R.id.number_of_left_activities2),
                    viewOfLayout.findViewById<TextView>(R.id.number_of_left_activities3),
                    viewOfLayout.findViewById<TextView>(R.id.number_of_left_activities4),
                    viewOfLayout.findViewById<TextView>(R.id.number_of_left_activities5)
                )
                //Get List of left activity titles
                var leftActivityTitles = listOf(viewOfLayout.findViewById<TextView>(R.id.left_activities1),
                    viewOfLayout.findViewById<TextView>(R.id.left_activities2),
                    viewOfLayout.findViewById<TextView>(R.id.left_activities3),
                    viewOfLayout.findViewById<TextView>(R.id.left_activities4),
                    viewOfLayout.findViewById<TextView>(R.id.left_activities5)
                )

                //Get List of right number
                var rightActivityNums = listOf(viewOfLayout.findViewById<TextView>(R.id.number_of_right_activities1),
                    viewOfLayout.findViewById<TextView>(R.id.number_of_right_activities2),
                    viewOfLayout.findViewById<TextView>(R.id.number_of_right_activities3),
                    viewOfLayout.findViewById<TextView>(R.id.number_of_right_activities4),
                    viewOfLayout.findViewById<TextView>(R.id.number_of_right_activities5)
                )
                //Get List of right activity titles
                var rightActivityTitles = listOf(viewOfLayout.findViewById<TextView>(R.id.right_activities1),
                    viewOfLayout.findViewById<TextView>(R.id.right_activities2),
                    viewOfLayout.findViewById<TextView>(R.id.right_activities3),
                    viewOfLayout.findViewById<TextView>(R.id.right_activities4),
                    viewOfLayout.findViewById<TextView>(R.id.right_activities5)
                )

                var leftCounter = 0
                var rightCounter = 0
                var activityInfo =
                    db.child("users").child(userID.toString()).get().addOnSuccessListener {
                        if (it.exists()) {

                            val allTheStuff = selectedItemsForDecisionTools
                            allTheStuff.forEach { (key, value) ->
                                Log.w("RUN THROUGH THIS: ","AGAIN!")
                                var tempName = key.value.toString().substringAfter("title=")
                                    .substringBefore("image_type=").trim()
                                var activitesOnLeftScreen = ""
                                var activitesOnRightScreen = ""
                                //Putting activities on top left or right of the screen
                                if (sectorsSize < 5) {
                                    if (value < 6) {
                                        activitesOnLeftScreen += "" + tempName + "\n"
                                    }
                                } else if (sectorsSize < 10) {
                                    if (value < 11) {
                                        activitesOnRightScreen += "" + tempName + "\n"
                                    }
                                }
                                if (sectorsSize < 10) {
                                    if (leftCounter<5){
                                        activityList.add(tempName)
                                        //Adding left activites text
                                        leftActivityTitles[leftCounter].setText(activitesOnLeftScreen)
                                        //Adding circles
                                        leftActivityNums[leftCounter].setText("" + (sectorsSize+1))
                                        leftActivityNums[leftCounter].setBackgroundResource(R.drawable.rounded_corner)
                                        leftActivityNums[leftCounter].setTextColor(purple_400)
                                        leftCounter++
                                    }
                                else{
                                    if (rightCounter<5){
                                        activityList.add(tempName)
                                        //Adding right activites text
                                        rightActivityTitles[rightCounter].setText(activitesOnRightScreen)
                                        //Adding circles
                                        rightActivityNums[rightCounter].setText("" + (sectorsSize+1))
                                        rightActivityNums[rightCounter].setBackgroundResource(R.drawable.rounded_corner)
                                        rightActivityNums[rightCounter].setTextColor(purple_400)
                                        rightCounter++
                                    }
                                }
                                    //Update counter
                                    //Log.w("", ""+sectorsSize)
                                    sectorsSize += 1
                                }
                            }
                        }
                        //Setting the correct wheel image
                        if (sectorsSize <= 1){
                            Log.w("", "----INVALID ACTIVTY AMOUNT----")
                        }
                        else if (sectorsSize == 2){
                            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_2)
                        }
                        else if (sectorsSize == 3){
                            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_3)
                        }
                        else if (sectorsSize == 4){
                            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_4)
                        }
                        else if (sectorsSize == 5){
                            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_5)
                        }
                        else if (sectorsSize == 6){
                            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_6)
                        }
                        else if (sectorsSize == 7){
                            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_7)
                        }
                        else if (sectorsSize == 8){
                            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_8)
                        }
                        else if (sectorsSize == 9){
                            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_9)
                        }
                        else if (sectorsSize == 10){
                            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_10)
                        }

                        ///
                        //Display activities on screen
                        //Setting the text views with the activites listed
                        //viewOfLayout.findViewById<TextView>(R.id.left_activities).setText(activitesOnLeftScreen)
                        //viewOfLayout.findViewById<TextView>(R.id.right_activities).setText(activitesOnRightScreen)
                        ///

                        // Mathematically divides up the wheel based on # of actions
                        sectorDegrees = IntArray(sectorsSize)

                        fun getDegreeForSectors(){
                            var sectorDegree : Int = 360/sectorsSize

                            for (i in activityList.indices) {
                                sectorDegrees[i] = (i + 1) * sectorDegree
                            }

                        }
                        getDegreeForSectors()
                    }
                // Variables for easy manipulation of objects in the activity_main.xml file   🙂
                var spinBtn: Button = viewOfLayout.findViewById<View>(R.id.Button) as Button
                wheel = viewOfLayout.findViewById<View>(R.id.wheel) as ImageView?
                //Log.w("Size of sectors", ""+sectorsSize)
                //Log.w("Size of sector degrees ", ""+sectorDegrees.size)
                spinBtn.setOnClickListener {
                    // Do some work here
                    if(!isSpinning){
                        Log.w("Status", "I entered the !spinning line")

                        // Does the animation work for the spin
                        // Alongside getting the random result
                        fun spin(){
                            degree = Random.nextInt(sectorsSize)

                            // Object that allows the rotation to work
                            // toDegrees is the major bit. Defines how far the rotation goes.
                            val rotate = RotateAnimation(
                                0F,
                                (360F * sectorsSize) + sectorDegrees[degree] + rotationOffsetForResult[sectorsSize-1],
                                RotateAnimation.RELATIVE_TO_SELF,
                                0.5f,
                                RotateAnimation.RELATIVE_TO_SELF,
                                0.5f
                            )
                            rotate.duration =  (2500) // time till the rotation animation stops
                            rotate.interpolator = LinearInterpolator()      // Used to make the animation smoother
                            rotate.fillAfter = true                         // If there is any mistake in degrees it'll fix it
                            rotate.setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationStart(animation: Animation) {

                                }
                                // Displays results of RNG via toast
                                override fun onAnimationEnd(animation: Animation) {
                                    //Log.w("DEGREE VAL ", degree.toString())
                                    //Log.w("SectorsSize: ", ""+sectorsSize)
                                    //Log.w("Activities Size: ", ""+activityList.size)
                                    //Log.w("", "You've got " + activityList[sectorsSize - (degree+1)])
                                    val handler = Handler(context!!.mainLooper)
                                    handler.post(Runnable {
                                        var tempAct = activityList[sectorsSize - (degree+1)]
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
                                    isSpinning = false
                                }

                                override fun onAnimationRepeat(animation: Animation) {}
                            })
                            wheel?.startAnimation(rotate) // finally starts the animation :)
                        }
                        spin()

                        isSpinning = true
                    }

                }

                var createEventToCalendarButton: Button = viewOfLayout.findViewById(R.id.CreateEvent)
                createEventToCalendarButton.setOnClickListener{
                    if (!selectedActivity.id.isNullOrEmpty()){
                       /*
                        //Clear the selected from the cart
                        selectedItemsForDecisionTools.forEach{ (key, value) ->
                            Log.w("VALUE: ",key.key.toString())
                            db.child("users").child(userID.toString()).child("data").child("cart")
                                .child(key.key.toString()).removeValue()
                        }
                        //Now load the calendar fragment
                        val fragmentManager = activity?.getSupportFragmentManager()
                        Log.w("IDK WHY YOU ARE HERE: ",fragmentManager.toString())
                        if (fragmentManager != null) {
                            fragmentManager.beginTransaction()
                                .replace(com.example.dsideapp.R.id.fragment_view, EventAddPopUpFragment()).commit()
                            Log.w("Made it here", "!")
                            pleaseWorkManager = fragmentManager
                        }
                        selectedItemsForDecisionTools.clear()
                        ppw.dismiss()*/
                        // MMMMM: Load the calendar fragment ------------------------------------------------
                        viewOfLayout = inflater.inflate(com.example.dsideapp.R.layout.fragment_eventadd_pop_up, null)
                        // create the popup window
                        val width = LinearLayout.LayoutParams.MATCH_PARENT
                        val height = LinearLayout.LayoutParams.MATCH_PARENT
                        val focusable = true // lets taps outside the popup also dismiss it
                        ppwWheel = PopupWindow(viewOfLayout, width, height, focusable)
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
                            val fragmentManager = activity?.getSupportFragmentManager()
                            Log.w("IDK WHY YOU ARE HERE: ",fragmentManager.toString())
                            if (fragmentManager != null) {
                                fragmentManager.beginTransaction()
                                    .replace(com.example.dsideapp.R.id.fragment_view, HomeFragment()).commit()
                                Log.w("Made it here", "!")
                                pleaseWorkManager = fragmentManager
                            }
                            selectedActivity = ActivityObject()
                            if (ppwCoin.isShowing){
                                ppwCoin.dismiss()
                            }
                            if (ppw.isShowing){
                                ppw.dismiss()
                            }
                            selectedItemsForDecisionTools.clear()

                            Log.d("AFTERIT", "{$cartActivityToAddToCalendarTEMP}")
                        }

                        /*var exitCalendarButton = viewOfLayout.findViewById<Button>(R.id.exitButton)
                        exitCalendarButton.setOnClickListener{
                            val fragmentManager = activity?.getSupportFragmentManager()
                            Log.w("IDK WHY YOU ARE HERE: ",fragmentManager.toString())
                            if (fragmentManager != null) {
                                fragmentManager.beginTransaction()
                                    .replace(com.example.dsideapp.R.id.fragment_view, HomeFragment()).commit()
                                Log.w("Made it here", "!")
                                pleaseWorkManager = fragmentManager
                            }
                            selectedActivity = ActivityObject()
                            if (ppwWheel.isShowing){
                                ppwWheel.dismiss()
                            }
                            if (ppw.isShowing){
                                ppw.dismiss()
                            }
                        }*/
                        // XXXXX -------------------------------------------------------------------------------------------------
                        // show the popup window
                        // which view you pass in doesn't matter, it is only used for the window token
                        ppwWheel.showAtLocation(view, Gravity.CENTER, 0, 0)
                        viewOfLayout.setOnTouchListener { v, event ->
                            ppwWheel.dismiss()
                            val fragmentManager = activity?.getSupportFragmentManager()
                            Log.w("IDK WHY YOU ARE HERE: ",fragmentManager.toString())
                            if (fragmentManager != null) {
                                fragmentManager.beginTransaction()
                                    .replace(com.example.dsideapp.R.id.fragment_view, HomeFragment()).commit()
                                Log.w("Made it here", "!")
                                pleaseWorkManager = fragmentManager
                            }
                            selectedActivity = ActivityObject()
                            if (ppwWheel.isShowing){
                                ppwWheel.dismiss()
                            }
                            if (ppw.isShowing){
                                ppw.dismiss()
                            }
                            true
                        }
                    }
                    else{
                        Toast.makeText(activity, "An activity hasn't been chosen yet!", Toast.LENGTH_SHORT).show()
                    }
                }
                ///
                return null
            }
        }

        getDBInfoForWheel().execute()
        return viewOfLayout
    }
    private fun explode() {
        viewKonfetti.start(Effects.explode())
    }
}