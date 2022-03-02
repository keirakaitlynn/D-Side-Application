package com.example.dsideapp.childfragments

import android.os.Bundle
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
import kotlin.random.Random

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
    var calendar: CalendarView? = null
    private var dateView: TextView? = null
    // array of the user's values. RN its just #'s but who cares
    private val sectors = arrayOf("Bowling", "Roasting Waters Boba", "Ping Pong", "Round1 Aracade", "Seal Beach",
        "Movie")
    // Offset to make sure the result aligns with the little green triangle
    //  Index 0 will never be used since there will always be 1+ things to spin.
    //  Needed for other calculations tho
    private val rotationOffsetForResult = arrayOf(0F,0F, 0F, 0F, 0F, -20F, -20F, -30F, -30F, -20F)

    // Roughly how big each user value's is represented on the wheel.
    private val sectorDegrees = IntArray(sectors.size)
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
        if (sectors.size == 2){
            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_2)
        }
        else if (sectors.size == 3){
            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_3)
        }
        else if (sectors.size == 4){
            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_4)
        }
        else if (sectors.size == 5){
            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_5)
        }
        else if (sectors.size == 6){
            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_6)
        }
        else if (sectors.size == 7){
            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_7)
        }
        else if (sectors.size == 8){
            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_8)
        }
        else if (sectors.size == 9){
            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_9)
        }
        else if (sectors.size == 10){
            viewOfLayout.findViewById<ImageView>(R.id.wheel).setImageResource(R.drawable.wheel_10)
        }
        // Variables for easy manipulation of objects in the activity_main.xml file   🙂
        var spinBtn: Button = viewOfLayout.findViewById<View>(R.id.Button) as Button
        wheel = viewOfLayout.findViewById<View>(R.id.wheel) as ImageView?
        getDegreeForSectors()
        Log.w("Size of sectors", ""+sectors.size)
        Log.w("Size of sector degrees ", ""+sectorDegrees.size)
        ///
        //Dispaly activities on screen
        var activitesOnLeftScreen = ""
        var activitesOnRightScreen = ""
        var counter = 0
        //Putting activities on top left or right of the screen
        sectors.forEach { act ->
            if (counter < 5){
                activitesOnLeftScreen += "" + (counter+1) + ": " + act + "\n"
            }
            else{
                activitesOnRightScreen += "" + (counter+1) + ": " + act + "\n"
            }
            //Update counter
            counter += 1
        }
        //Resetting the counter
        counter = 0
        //Setting the text views with the activites listed
        viewOfLayout.findViewById<TextView>(R.id.left_activities).setText(activitesOnLeftScreen)
        viewOfLayout.findViewById<TextView>(R.id.right_activities).setText(activitesOnRightScreen)
        ///
        spinBtn.setOnClickListener {
            // Do some work here
            if(!isSpinning){
                Log.w("Status", "I entered the !spinning line")
                spin()
                isSpinning = true
            }

        }

        return viewOfLayout
    }


    // Mathematically divides up the wheel based on # of actions
    private fun getDegreeForSectors(){
        var sectorDegree : Int = 360/sectors.size

        for (i in sectors.indices) {
            sectorDegrees[i] = (i + 1) * sectorDegree
        }

    }

    // Does the animation work for the spin
    // Alongside getting the random result
    private fun spin(){
        degree = Random.nextInt(sectors.size)

        // Object that allows the rotation to work
        // toDegrees is the major bit. Defines how far the rotation goes.
        val rotate = RotateAnimation(
            0F,
            (360F * sectors.size) + sectorDegrees[degree] + rotationOffsetForResult[sectors.size-1],
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

                Toast.makeText(activity, "You've got " + sectors[sectors.size - (degree+1)], Toast.LENGTH_SHORT).show()
                //Toast.makeText(activity, "You've got " + sectors[sectors.size - (degree+1)] + " stuffs", Toast.LENGTH_SHORT).show()
                isSpinning = false
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        wheel?.startAnimation(rotate) // finally starts the animation :)
    }
}