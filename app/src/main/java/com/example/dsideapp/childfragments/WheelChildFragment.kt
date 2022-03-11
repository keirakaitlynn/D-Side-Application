package com.example.dsideapp.childfragments

import android.content.Context
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

    private lateinit var viewOfLayout: View

    // Roughly how big each user value's is represented on the wheel.
    private lateinit var sectorDegrees: IntArray
    var calendar: CalendarView? = null
    private var dateView: TextView? = null

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

        class getDBInfoForWheel : AsyncTask<Void, Void, Void>() {
            // array of the user's values. RN its just #'s but who cares
            var sectors = ""
            // size of sector
            var sectorsSize = 0
            // list of all the activities
            var activityList = mutableListOf<String>()
            var activitesOnLeftScreen = ""
            var activitesOnRightScreen = ""

            override fun doInBackground(vararg params: Void): Void? {
                ///Grabbing all the activities from DB to populate screen
                //Populate an array to with DB Cart activities
                var activityInfo =
                    db.child("users").child(userID.toString()).get().addOnSuccessListener {
                        if (it.exists()) {
                            val allTheStuff = it.child("data").child("cart").children
                            allTheStuff.forEach { act ->
                                //Putting activities on top left or right of the screen
                                if (sectorsSize < 5) {
                                    activitesOnLeftScreen += "" + (sectorsSize + 1) + ": " + act.child("title").value.toString() + "\n"
                                } else {
                                    activitesOnRightScreen += "" + (sectorsSize + 1) + ": " + act.child(
                                        "title"
                                    ).value.toString() + "\n"
                                }
                                activityList.add(0,act.child("title").value.toString())
                                //Update counter
                                //Log.w("", ""+sectorsSize)
                                sectorsSize += 1
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
                        viewOfLayout.findViewById<TextView>(R.id.left_activities).setText(activitesOnLeftScreen)
                        viewOfLayout.findViewById<TextView>(R.id.right_activities).setText(activitesOnRightScreen)
                        ///

                        // Mathematically divides up the wheel based on # of actions
                        fun getDegreeForSectors(){
                            var sectorDegree : Int = 360/sectorsSize

                            for (i in sectors.indices) {
                                sectorDegrees[i] = (i + 1) * sectorDegree
                            }

                        }
                        getDegreeForSectors()


                        sectorDegrees = IntArray(sectorsSize)
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
                                    Log.w("DEGREE VAL ", degree.toString())
                                    Log.w("SectorsSize: ", ""+sectorsSize)
                                    Log.w("Activities Size: ", ""+activityList.size)
                                    Log.w("", "You've got " + activityList[sectorsSize - (degree+1)])
                                    val handler = Handler(context!!.mainLooper)
                                    handler.post(Runnable {
                                        Toast.makeText(activity, "You've got " + activityList[sectorsSize - (degree+1)] + " stuffs", Toast.LENGTH_SHORT).show()
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
                ///
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                val toast = Toast.makeText(activity, "You've got Mail", Toast.LENGTH_SHORT)
                toast.show()

                //Toast.makeText(activity, "You've got " + activityList[sectorsSize - (degree+1)], Toast.LENGTH_SHORT).show()
            }
        }

        getDBInfoForWheel().execute()
        val toast = Toast.makeText(activity, "You've got Mail", Toast.LENGTH_SHORT)
        toast.show()
        return viewOfLayout
    }
}