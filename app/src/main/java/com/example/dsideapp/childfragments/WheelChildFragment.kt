package com.example.dsideapp.childfragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList
import kotlin.random.Random
import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.dsideapp.R.color
import com.example.dsideapp.R.color.*
import com.example.dsideapp.data.Effects
import nl.dionsegijn.konfetti.xml.KonfettiView
import com.example.dsideapp.data.selectedItemsForDecisionTools
import com.example.dsideapp.fragments.CreatePollFragment
import com.example.dsideapp.fragments.selectedActivity
import com.example.dsideapp.fragments.ppw

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
                        ppw.dismiss()
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