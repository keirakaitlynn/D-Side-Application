package com.example.dsideapp.childfragments

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
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class DiceChildFragment : Fragment() {

    private var imageViewDice: ImageView? = null
    private val rng = Random()
    private lateinit var viewOfLayout: View

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
        class getDBInfoForDice : AsyncTask<Void, Void, Void>() {
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
                                if (activityList.size < 3) {
                                    activitesOnLeftScreen += "" + (activityList.size + 1) + ": " + act.child("title").value.toString() + "\n"
                                } else {
                                    activitesOnRightScreen += "" + (activityList.size + 1) + ": " + act.child(
                                        "title"
                                    ).value.toString() + "\n"
                                }
                                activityList.add(act.child("title").value.toString())
                            }
                        }
                        /////
                        //Display activities on screen
                        //Setting the text views with the activites listed
                        viewOfLayout.findViewById<TextView>(R.id.left_activities_dice).setText(activitesOnLeftScreen)
                        viewOfLayout.findViewById<TextView>(R.id.right_activities_dice).setText(activitesOnRightScreen)
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
                        Toast.makeText(activity, "You've got " + activityList[randomNum], Toast.LENGTH_SHORT).show()
                    })
                }

                return null
            }
        }
        getDBInfoForDice().execute()

        return viewOfLayout
    }
}