package com.example.dsideapp.childfragments

import android.graphics.Camera
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.data.*
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import nl.dionsegijn.konfetti.xml.KonfettiView
import com.example.dsideapp.data.Effects
import com.example.dsideapp.data.selectedItemsForDecisionTools
import com.example.dsideapp.fragments.HomeFragment
import com.example.dsideapp.fragments.selectedActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.ktx.Firebase
import kotlin.random.Random
import com.example.dsideapp.fragments.ppw

var ppwCoin = PopupWindow()

class CoinChildFragment : Fragment() {
    private var pleaseWorkManager : FragmentManager? = null
    private var selectedActivityID: String? = ""
    private lateinit var viewKonfetti: KonfettiView
    private var coin: ImageView? = null
    private var btn: Button? = null
    private lateinit var viewOfLayout: View
    private var isHeads = false
    //getting database info
    var authorization = auth
    var user = authorization.currentUser
    var userID = authorization.currentUser?.uid
    var db = FirebaseDatabase.getInstance().getReference()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var v = inflater.inflate(R.layout.fragment_child_coin, container, false)
        var activityList = mutableListOf<String>()
        var activitesOnLeftScreen = ""
        var activitesOnRightScreen = ""
        var numberForActivitesOnLeftScreen = ""
        var numberForActivitesOnRightScreen = ""
        viewKonfetti = v.findViewById(R.id.konfettiView)

        v.findViewById<TextView>(R.id.Result).visibility = View.GONE
        ///Grabbing all the activities from DB to populate screen
        //Populate an array to with DB Cart activities
        var activityInfo =
            db.child("users").child(userID.toString()).get().addOnSuccessListener {
                if (it.exists()) {
                    val allTheStuff = selectedItemsForDecisionTools
                    allTheStuff.forEach { (key, value) ->
                        //Putting activities on top left or right of the screen
                        var tempName = key.value.toString().substringAfter("title=")
                            .substringBefore("image_type=").trim()
                        if (activityList.size < 1) {
                            if (value == 1){
                                activitesOnLeftScreen += "" + tempName + "\n"
                                numberForActivitesOnLeftScreen += "" + (activityList.size + 1)
                            }
                        } else if (activityList.size < 2){
                            if (value == 2){
                                activitesOnRightScreen += "" + tempName + "\n"
                                numberForActivitesOnRightScreen += "" + (activityList.size + 1)
                            }
                        }
                        if (activityList.size < 2) {
                            activityList.add(tempName)
                        }
                    }
                }
                //Display activities on screen
                //Setting the text views with the activites listed
                Log.w("XD", "left" + activitesOnLeftScreen)
                Log.w("XD", "Right" + activitesOnRightScreen)
                v.findViewById<TextView>(R.id.left_activities_coin).setText(activitesOnLeftScreen)
                v.findViewById<TextView>(R.id.number_for_left_activities_coin).setText(numberForActivitesOnLeftScreen)
                v.findViewById<TextView>(R.id.right_activities_coin).setText(activitesOnRightScreen)
                v.findViewById<TextView>(R.id.number_for_right_activities_coin).setText(numberForActivitesOnRightScreen)
                //v.findViewById<TextView>(R.id.right_activities_coin).setText(activitesOnRightScreen)
                Log.w("XD", activitesOnLeftScreen)
            }

        var curSide = R.drawable.heads
        var isheads = true
        coin = v.findViewById<View>(R.id.coin) as ImageView
        btn = v.findViewById<View>(R.id.btn) as Button
        btn!!.setOnClickListener {
            val stayTheSame = kotlin.random.Random.Default.nextBoolean()
            val animation: Rotate3dAnimation
            if (curSide == R.drawable.heads) {
                animation = Rotate3dAnimation(coin!!, R.drawable.heads, R.drawable.tails, 0f, 180f, 0f, 0f, 0f, 0f)
            } else {
                animation = Rotate3dAnimation(coin!!, R.drawable.tails, R.drawable.heads, 0f, 180f, 0f, 0f, 0f, 0f)
            }
            if (stayTheSame) {
                animation.repeatCount = 5 // must be odd (5+1 = 6 flips so the side will stay the same)
                if (isheads) {
                    System.out.println("heads to heads")
                    isheads = true
                    curSide = R.drawable.heads
                }
                else {
                    System.out.println("tails to Tails")
                    isheads = false
                    curSide = R.drawable.tails
                }
            } else {
                animation.repeatCount = 6 // must be even (6+1 = 7 flips so the side will not stay the same)
                if (isheads) {
                    System.out.println("heads to tails")
                    isheads = false
                    curSide = R.drawable.tails
                }
                else {
                    System.out.println("tails to heads")
                    isheads = true
                    curSide = R.drawable.heads
                }
            }



            isHeads = isheads
            animation.duration = 110
            animation.interpolator = LinearInterpolator()
            coin!!.startAnimation(animation)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {
                    v.findViewById<TextView>(R.id.Result).text = ""
                    v.findViewById<TextView>(R.id.Result).visibility = View.GONE
                }
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {

                    val sol = listOf(activityList[0],activityList[1])
                    var solution = if (isHeads.toString() == "true")  {"heads: " + sol[0]} else {"tails: " + sol[1]}
                    Log.w("RAWR ", solution )

                    v.findViewById<TextView>(R.id.Result).text = solution
                    v.findViewById<TextView>(R.id.Result).visibility = View.VISIBLE

                    var tempAct = solution.split(' ')[1].trim()
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
                }
            })

        }

        var authorization = auth
        var user = authorization.currentUser
        var userID = authorization.currentUser?.uid
        var db = FirebaseDatabase.getInstance().getReference()

        var createEventToCalendarButton: Button = v.findViewById(R.id.CreateEvent)
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
                ppw.dismiss()
                */
                // MMMMM: Load the calendar fragment ------------------------------------------------
                v = inflater.inflate(com.example.dsideapp.R.layout.fragment_eventadd_pop_up, null)
                // create the popup window
                val width = LinearLayout.LayoutParams.MATCH_PARENT
                val height = LinearLayout.LayoutParams.MATCH_PARENT
                val focusable = true // lets taps outside the popup also dismiss it
                ppwCoin = PopupWindow(v, width, height, focusable)
                // XXXXX -------------------------------------------------------------------------------------------------



                val datePicker = v.findViewById<DatePicker>(R.id.datePicker)
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
                var createEventButton = v.findViewById<Button>(R.id.addEventButton)
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
                            //var eventTitle = v.findViewById<TextView>(R.id.eventName).text.toString()
                            //var eventDate = viewOfLayout.findViewById<DatePicker>(R.id.datePicker)
                            val day = datePicker.dayOfMonth
                            val month = datePicker.month
                            val year = datePicker.year

                            var eventTime = v.findViewById<TextView>(R.id.TimeText)

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
                                eventId += Random.nextInt(9)
                            }
                            for (i in 1..5) {
                                eventId += (Random.nextInt(25) + 65).toChar()
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

                /*var exitCalendarButton = v.findViewById<Button>(R.id.exitButton)
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
                    if (ppwCoin.isShowing){
                        ppwCoin.dismiss()
                    }
                    if (ppw.isShowing){
                        ppw.dismiss()
                    }
                }*/

                // XXXXX -------------------------------------------------------------------------------------------------
                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window token
                ppwCoin.showAtLocation(view, Gravity.CENTER, 0, 0)
                v.setOnTouchListener { v, event ->
                    ppwCoin.dismiss()
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
                    true
                }
                // ----------------------------------------------------------------------------------
//                //Clear the selected from the cart
//                selectedItemsForDecisionTools.forEach{ (key, value) ->
//                    Log.w("VALUE: ",key.key.toString())
//                    db.child("users").child(userID.toString()).child("data").child("cart")
//                        .child(key.key.toString()).removeValue()
//                }
                // Clear the randomly decided/selected Activity from cart
//                val deleted = db.child("users").child(userID.toString()).child("data").child("cart")
//                    .child(selectedActivityID.toString()).removeValue()
//                Log.d("DELETED", "${deleted}")
            }
            else{
                Toast.makeText(activity, "An activity hasn't been chosen yet!", Toast.LENGTH_SHORT).show()
            }
        }

        return v
    }

    class Rotate3dAnimation(
        private val imageView: ImageView,
        private var curDrawable: Int,
        private var nextDrawable: Int,
        private val fromXDegrees: Float,
        private val toXDegrees: Float,
        private val fromYDegrees: Float,
        private val toYDegrees: Float,
        private val fromZDegrees: Float,
        private val toZDegrees: Float
    ) :
        Animation() {
        private var camera: Camera? = null
        private var width = 0
        private var height = 0
        private var numOfRepetition = 0
        private var repeatCount = 0f
        override fun setRepeatCount(repeatCount: Int) {
            super.setRepeatCount(repeatCount)
            this.repeatCount = (repeatCount + 1).toFloat()
        }

        override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
            super.initialize(width, height, parentWidth, parentHeight)
            this.width = width / 2
            this.height = height / 2
            camera = Camera()
        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            var xDegrees = fromXDegrees + (toXDegrees - fromXDegrees) * interpolatedTime
            val yDegrees = fromYDegrees + (toYDegrees - fromYDegrees) * interpolatedTime
            val zDegrees = fromZDegrees + (toZDegrees - fromZDegrees) * interpolatedTime
            val matrix: Matrix = t.matrix


            // ----------------- ZOOM ----------------- //
            if ((numOfRepetition + interpolatedTime) / (repeatCount / 2) <= 1) {
                imageView.scaleX = 1 + (numOfRepetition + interpolatedTime) / (repeatCount / 2)
                imageView.scaleY = 1 + (numOfRepetition + interpolatedTime) / (repeatCount / 2)
            } else if (numOfRepetition < repeatCount) {
                imageView.scaleX = 3 - (numOfRepetition + interpolatedTime) / (repeatCount / 2)
                imageView.scaleY = 3 - (numOfRepetition + interpolatedTime) / (repeatCount / 2)
            }


            // ----------------- ROTATE ----------------- //
            //System.err.println(interpolatedTime)
            if (interpolatedTime >= 0.5f) {
                if (interpolatedTime == 1f) {
                    val temp = curDrawable
                    curDrawable = nextDrawable
                    nextDrawable = temp
                    numOfRepetition++
                } else {
                    imageView.setImageResource(nextDrawable)
                }
                xDegrees -= 180f
            } else if (interpolatedTime == 0f) {
                imageView.setImageResource(curDrawable)
            }
            camera!!.save()
            camera!!.rotateX(-xDegrees)
            camera!!.rotateY(yDegrees)
            camera!!.rotateZ(zDegrees)
            camera!!.getMatrix(matrix)
            camera!!.restore()
            matrix.preTranslate(-width.toFloat(), -height.toFloat())
            matrix.postTranslate(width.toFloat(), height.toFloat())
        }
    }

    private fun explode() {
        viewKonfetti.start(Effects.explode())
    }
}

/*package com.example.dsideapp.childfragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import java.util.*
import android.graphics.Matrix
import android.view.animation.*
import android.view.animation.Transformation
import android.graphics.Camera
import android.view.animation.LinearInterpolator
class CoinChildFragment : Fragment() {
    private var coin: ImageView? = null
    private var btn: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var curSide = R.drawable.heads
        var isheads = true
        val v = inflater.inflate(R.layout.fragment_child_coin, container, false)
        coin = v.findViewById<View>(R.id.coin) as ImageView
        btn = v.findViewById<View>(R.id.btn) as Button
        btn!!.setOnClickListener {
            val stayTheSame = kotlin.random.Random.Default.nextBoolean()
            val animation: Rotate3dAnimation
            if (curSide == R.drawable.heads) {
                animation = Rotate3dAnimation(coin!!, R.drawable.heads, R.drawable.tails, 0f, 180f, 0f, 0f, 0f, 0f)
            } else {
                animation = Rotate3dAnimation(coin!!, R.drawable.tails, R.drawable.heads, 0f, 180f, 0f, 0f, 0f, 0f)
            }
            if (stayTheSame) {
                animation.repeatCount = 5 // must be odd (5+1 = 6 flips so the side will stay the same)
                if (isheads) {
                    System.out.println("heads to heads")
                    isheads = true
                    curSide = R.drawable.heads
                }
                else {
                    System.out.println("tails to Tails")
                    isheads = false
                    curSide = R.drawable.tails
                }
            } else {
                animation.repeatCount = 6 // must be even (6+1 = 7 flips so the side will not stay the same)
                if (isheads) {
                    System.out.println("heads to tails")
                    isheads = false
                    curSide = R.drawable.tails
                }
                else {
                    System.out.println("tails to heads")
                    isheads = true
                    curSide = R.drawable.heads
                }
            }
            animation.duration = 110
            animation.interpolator = LinearInterpolator()
            coin!!.startAnimation(animation)
        }
        return v
        }
    // IGNORE EVERYTHING BELOW THIS. CLASS ROTATE3DANIMATION IS SIMPLY TO FLIP THE COIN. IF YOU NEED TO **CHANGE** HOW COIN FLIPS, THEN LOOK BELOW AND PROCEED WITH CAUTION.
    class Rotate3dAnimation(
        private val imageView: ImageView,
        private var curDrawable: Int,
        private var nextDrawable: Int,
        private val fromXDegrees: Float,
        private val toXDegrees: Float,
        private val fromYDegrees: Float,
        private val toYDegrees: Float,
        private val fromZDegrees: Float,
        private val toZDegrees: Float
    ) :
        Animation() {
        private var camera: Camera? = null
        private var width = 0
        private var height = 0
        private var numOfRepetition = 0
        private var repeatCount = 0f
        override fun setRepeatCount(repeatCount: Int) {
            super.setRepeatCount(repeatCount)
            this.repeatCount = (repeatCount + 1).toFloat()
        }
        override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
            super.initialize(width, height, parentWidth, parentHeight)
            this.width = width / 2
            this.height = height / 2
            camera = Camera()
        }
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            var xDegrees = fromXDegrees + (toXDegrees - fromXDegrees) * interpolatedTime
            val yDegrees = fromYDegrees + (toYDegrees - fromYDegrees) * interpolatedTime
            val zDegrees = fromZDegrees + (toZDegrees - fromZDegrees) * interpolatedTime
            val matrix: Matrix = t.matrix
            // ----------------- ZOOM ----------------- //
            if ((numOfRepetition + interpolatedTime) / (repeatCount / 2) <= 1) {
                imageView.scaleX = 1 + (numOfRepetition + interpolatedTime) / (repeatCount / 2)
                imageView.scaleY = 1 + (numOfRepetition + interpolatedTime) / (repeatCount / 2)
            } else if (numOfRepetition < repeatCount) {
                imageView.scaleX = 3 - (numOfRepetition + interpolatedTime) / (repeatCount / 2)
                imageView.scaleY = 3 - (numOfRepetition + interpolatedTime) / (repeatCount / 2)
            }
            // ----------------- ROTATE ----------------- //
            if (interpolatedTime >= 0.5f) {
                if (interpolatedTime == 1f) {
                    val temp = curDrawable
                    curDrawable = nextDrawable
                    nextDrawable = temp
                    numOfRepetition++
                } else {
                    imageView.setImageResource(nextDrawable)
                }
                xDegrees -= 180f
            } else if (interpolatedTime == 0f) {
                imageView.setImageResource(curDrawable)
            }
            camera!!.save()
            camera!!.rotateX(-xDegrees)
            camera!!.rotateY(yDegrees)
            camera!!.rotateZ(zDegrees)
            camera!!.getMatrix(matrix)
            camera!!.restore()
            matrix.preTranslate(-width.toFloat(), -height.toFloat())
            matrix.postTranslate(width.toFloat(), height.toFloat())
        }
    }
    companion object {
        val RANDOM = Random()
    }
}*/