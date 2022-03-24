package com.example.dsideapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.data.ActivityObject
import com.example.dsideapp.data.LocationObject
import com.example.dsideapp.data.PollObject
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.random.Random

class PollsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val v = inflater.inflate(R.layout.fragment_polls, container, false)
        var pollId: String
        val pollPosterId: String
        val pollOptions: MutableList<String>
        val pollVoteCount: MutableList<Int>
        var pollEndTime: Int
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

        //Getting the poll options
        var createButton = v.findViewById<Button>(R.id.pollCreateButton)
        var option1 = v.findViewById<EditText>(R.id.pollOption1)
        var option2 = v.findViewById<EditText>(R.id.pollOption2)
        var option3 = v.findViewById<EditText>(R.id.pollOption3)
        var option4 = v.findViewById<EditText>(R.id.pollOption4)
        var option5 = v.findViewById<EditText>(R.id.pollOption5)
        var option6 = v.findViewById<EditText>(R.id.pollOption6)
        var pollTime = v.findViewById<EditText>(R.id.pollTime)
        pollOptions = mutableListOf()
        pollVoteCount = mutableListOf()

        createButton.setOnClickListener {
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
            if (pollTime.text.toString() == "Time") {
                pollEndTime = 5
            } else {
                pollEndTime = pollTime.text.toString().toInt()
            }
            //Filling oll vote count list with 6 elements
            while (pollVoteCount.size < 6) {
                pollVoteCount.add(0)
            }
            //Creating a poll
            data class stringPoll(
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
                val poll_Time: String? = null
            ) {}

            var newPoll = PollObject(
                pollId,
                pollPosterId,
                pollOptions,
                pollVoteCount,
                pollEndTime,
                business_name = "None",
                winner_index = 0
            )

            var dbReadablePoll = stringPoll(
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
                pollEndTime.toString()
            )

            //For true functionality, set random list of characters to "userId" to properly write to currently logged in user. As well, set name in ".child(name)" as an ID in the future to make it easier to search and read from DB.
            db.child(pollId).setValue(dbReadablePoll)
        }
        return v
    }


}