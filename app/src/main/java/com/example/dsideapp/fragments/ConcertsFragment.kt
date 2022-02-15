package com.example.dsideapp.fragments

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

class ConcertsFragment : Fragment() {
    private lateinit var viewOfLayout: View
    var calendar: CalendarView? = null
    private var dateView: TextView? = null
    // array of the user's values. RN its just #'s but who cares
    private val sectors = arrayOf("1","2", "3", "4", "5", "6", "7", "8", "9", "10")
    // Offset to make sure the result aligns with the little green triangle
    //  Index 0 will never be used since there will always be 1+ things to spin.
    //  Needed for other calculations tho
    private val rotationOffsetForResult = arrayOf(0F,0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, -106F)

    // Roughly how big each user value's is represented on the wheel.
    private val sectorDegrees = IntArray(sectors.size)
    // Degree that represents the location of the random result
    private var degree : Int = 0
    var isSpinning : Boolean = false
    // Wheel image taken from the XML
    private var wheel: ImageView? = null

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_concerts, container, false)
        // Variables for easy manipulation of objects in the activity_main.xml file   🙂
        var spinBtn: Button = viewOfLayout.findViewById<View>(R.id.Button) as Button
        wheel = viewOfLayout.findViewById<View>(R.id.wheel) as ImageView?
        getDegreeForSectors()
        Log.w("Size of sectors", ""+sectors.size)
        Log.w("Size of sector degrees ", ""+sectorDegrees.size)
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

                Toast.makeText(activity, "You've got " + sectors[sectors.size - (degree+1)] + " stuffs", Toast.LENGTH_SHORT).show()
                isSpinning = false
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        wheel?.startAnimation(rotate) // finally starts the animation :)
    }
}