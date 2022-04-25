package com.example.dsideapp.childfragments

import android.annotation.SuppressLint
import android.media.Image
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.dsideapp.auth
import com.example.dsideapp.data.Effects
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import nl.dionsegijn.konfetti.xml.KonfettiView
import android.view.animation.*
import androidx.fragment.app.FragmentManager
import com.example.dsideapp.data.selectedItemsForDecisionTools
import com.example.dsideapp.fragments.ppw
import com.example.dsideapp.fragments.selectedActivity
import com.google.firebase.database.DataSnapshot
import com.example.dsideapp.fragments.ppw

class DiceChildFragment : Fragment() {

    private var pleaseWorkManager : FragmentManager? = null
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