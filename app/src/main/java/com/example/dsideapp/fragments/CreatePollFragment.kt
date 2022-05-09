package com.example.dsideapp.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.childfragments.SuggestionsChildFragment
import com.example.dsideapp.data.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.random.Random
import java.security.Timestamp
private val createThePollFragment = CreatePollFragment()

class CreatePollFragment : Fragment() {
    private var adapter: RecyclerView.Adapter<RecyclerAdapterForTitles.ViewHolder>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var pleaseWorkManager : FragmentManager? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_create_poll, container, false)
        //Getting poster's ID
        var authorization = auth
        var user = authorization.currentUser
        var userID = authorization.currentUser?.uid
        var db = FirebaseDatabase.getInstance().getReference("Public Polls")

        //Transition to create
       // val createThePollButton = v.findViewById<Button>(R.id.pollCreateButton)
            // inflate the layout of the popup window
           // v = inflater.inflate(com.example.dsideapp.R.layout.fragment_create_poll, null)
            // create the popup window
            //val width = LinearLayout.LayoutParams.MATCH_PARENT
            //val height = LinearLayout.LayoutParams.WRAP_CONTENT
            //val focusable = true // lets taps outside the popup also dismiss it
            //val popupWindow = PopupWindow(v, width, height, focusable)

            // show the popup window
            //popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

            var exitButton = v.findViewById<Button>(R.id.exitPollCreateButton)
            exitButton.setOnClickListener{
                //popupWindow.dismiss()
                //true
                val fragmentManager = activity?.getSupportFragmentManager()
                Log.w("IDK WHY YOU ARE HERE: ",fragmentManager.toString())
                if (fragmentManager != null) {
                    fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view,  PollsFragment()).commit()
                    Log.w("Made it here","!")
                    pleaseWorkManager = fragmentManager
                }
            }

            var createButton = v.findViewById<Button>(R.id.pollCreateButton)
            ////CREATE POLL////
            createButton.setOnClickListener {
                var pollId: String
                val pollPosterId: String
                val pollOptions: MutableList<String>
                val pollVoteCount: MutableList<Int>
                var pollEndTime: Long
                val businessName: String
                var winnerIndex: Int

                //Creating the random poll ID
                var i = 0
                pollId = ""
                for(i in 1..5){
                    pollId += Random.nextInt(9)
                }
                for(i in 1..5){
                    pollId += (Random.nextInt(25) + 65).toChar()
                }

                //Getting poster's ID
                var authorization = auth
                var user = authorization.currentUser
                var userID = authorization.currentUser?.uid
                var db = FirebaseDatabase.getInstance().getReference("Public Polls")
                pollPosterId = userID.toString()

                //Getting the poll title
                var pollTitleInPopUp = v.findViewById<EditText>(R.id.pollUserTitle)
                //Getting the poll options
                var option1 = v.findViewById<EditText>(R.id.pollOption1)
                var option2 = v.findViewById<EditText>(R.id.pollOption2)
                var option3 = v.findViewById<EditText>(R.id.pollOption3)
                var option4 = v.findViewById<EditText>(R.id.pollOption4)
                var option5 = v.findViewById<EditText>(R.id.pollOption5)
                var option6 = v.findViewById<EditText>(R.id.pollOption6)
                var pollTime = v.findViewById<EditText>(R.id.pollTime)
                pollOptions = mutableListOf()
                pollVoteCount = mutableListOf()
                var votersList = ""

                //Getting the options for the poll
                pollOptions.add(option1.text.toString())
                pollOptions.add(option2.text.toString())
                pollOptions.add(option3.text.toString())
                pollOptions.add(option4.text.toString())
                pollOptions.add(option5.text.toString())
                pollOptions.add(option6.text.toString())
                //Making sure poll options, guaranteed, has 6 elements
                while (pollOptions.size < 6) {
                    pollOptions.add("None")
                }
                //Getting the poll time in minutes
                if (pollTime.text.toString() == "Time" || !pollTime.text.isDigitsOnly() || pollTime.text.isNullOrEmpty()) {
                    //5 min base. 5 min * 60sec/min * 1000 millisec/sec + current time.
                    pollEndTime = Date(5 * 60 * 1000 + Date().getTime()).time
                } else {
                    //Input (GIVEN THAT IT IS IN MINUTES) * 60sec/min * 1000 millisec/sec + current time.
                    pollEndTime = Date(pollTime.text.toString().toLong() * 60 * 1000 + Date().getTime()).time
                }
                //Filling oll vote count list with 6 elements
                while (pollVoteCount.size < 6) {
                    pollVoteCount.add(0)
                }
                //Creating a poll
                data class stringPoll(
                    val poll_TITLE: String? = null,
                    val poll_ID: String? = null,
                    val poster_ID: String? = null,
                    val opt1: String? = null,
                    val opt2: String? = null,
                    val opt3: String? = null,
                    val opt4: String? = null,
                    val opt5: String? = null,
                    val opt6: String? = null,
                    val opt1Vote: String? = null,
                    val opt2Vote: String? = null,
                    val opt3Vote: String? = null,
                    val opt4Vote: String? = null,
                    val opt5Vote: String? = null,
                    val opt6Vote: String? = null,
                    val poll_Time: String? = null,
                    val voters: String? = null
                ) {}


                var dbReadablePoll = stringPoll(
                    pollTitleInPopUp.text.toString(),
                    pollId,
                    pollPosterId,
                    pollOptions.get(0),
                    pollOptions.get(1),
                    pollOptions.get(2),
                    pollOptions.get(3),
                    pollOptions.get(4),
                    pollOptions.get(5),
                    pollVoteCount.get(0).toString(),
                    pollVoteCount.get(1).toString(),
                    pollVoteCount.get(2).toString(),
                    pollVoteCount.get(3).toString(),
                    pollVoteCount.get(4).toString(),
                    pollVoteCount.get(5).toString(),
                    pollEndTime.toString(),
                    votersList
                )

                //For true functionality, set random list of characters to "userId" to properly write to currently logged in user. As well, set name in ".child(name)" as an ID in the future to make it easier to search and read from DB.
                db.child(pollId).setValue(dbReadablePoll)
                //popupWindow.dismiss()
                true
                ////MIGHT NOT NEED
                val fragmentManager = activity?.getSupportFragmentManager()
                Log.w("IDK WHY YOU ARE HERE: ",fragmentManager.toString())
                if (fragmentManager != null) {
                    fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view,  PollsFragment()).commit()
                    Log.w("Made it here","!")
                    pleaseWorkManager = fragmentManager
                }
                //Log.w("Twas null","!")
                ////MIGHT NOT NEED
            }

        return v
    }
}