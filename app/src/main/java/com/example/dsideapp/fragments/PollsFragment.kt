package com.example.dsideapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.childfragments.SuggestionsChildFragment
import com.example.dsideapp.data.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.random.Random
private val createThePollFragment = CreatePollFragment()

class PollsFragment : Fragment() {
    private var adapter: RecyclerView.Adapter<RecyclerAdapterForTitles.ViewHolder>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_polls, container, false)
        //Getting poster's ID
        var authorization = auth
        var user = authorization.currentUser
        var userID = authorization.currentUser?.uid
        var db = FirebaseDatabase.getInstance().getReference("Public Polls")

        //Creating the vars for the create popup
        lateinit var createPollPopUpTitle: TextView
        lateinit var createPollPopUpOpt1: TextView
        lateinit var createPollPopUpOpt2: TextView
        lateinit var createPollPopUpOpt3: TextView
        lateinit var createPollPopUpOpt4: TextView
        lateinit var createPollPopUpOpt5: TextView
        lateinit var createPollPopUpOpt6: TextView
        lateinit var createPollPopUpTime: TextView

        //Transition to create
        val createThePollButton = v.findViewById<Button>(R.id.pollCreateButton)
        createThePollButton.setOnClickListener{
            // inflate the layout of the popup window
            v = inflater.inflate(com.example.dsideapp.R.layout.fragment_create_poll, null)
            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(v, width, height, focusable)

            // show the popup window
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

            var exitButton = v.findViewById<Button>(R.id.exitPollCreateButton)
            exitButton.setOnClickListener{
                popupWindow.dismiss()
                true
            }

            var createButton = v.findViewById<Button>(R.id.pollCreateButton)

            ////CREATE POLL////
            createButton.setOnClickListener {
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
                        val poll_Time: String? = null
                    ) {}

                    var newPoll = PollObject(
                        pollId,
                        pollPosterId,
                        pollOptions,
                        pollVoteCount,
                        pollEndTime,
                        pollTitleInPopUp.text.toString(),
                        winner_index = 0
                    )

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
                        pollEndTime.toString()
                    )

                    //For true functionality, set random list of characters to "userId" to properly write to currently logged in user. As well, set name in ".child(name)" as an ID in the future to make it easier to search and read from DB.
                    db.child(pollId).setValue(dbReadablePoll)
                popupWindow.dismiss()
                true
            }
            ////////


        }
        //-----------------------------
        //Iterate through every currently active poll
        var pollInfo = db.get().addOnSuccessListener {
            if (it.exists()) {
                // for loop going through all the active polls
                val allPolls = it.children
                val rv = v.findViewById<RecyclerView>(R.id.pollsRecyclerView)
                val pollViews = mutableListOf<String>()
                layoutManager = LinearLayoutManager(requireContext())
                rv.layoutManager = layoutManager
                adapter = RecyclerAdapterForTitles(requireContext(), pollViews)
                rv.adapter = adapter
                allPolls.forEach { poll ->
                    //Getting poll options from db
                    val pollOpt1 = poll.child("opt1").value
                    val pollOpt2 = poll.child("opt2").value
                    val pollOpt3 = poll.child("opt3").value
                    val pollOpt4 = poll.child("opt4").value
                    val pollOpt5 = poll.child("opt5").value
                    val pollOpt6 = poll.child("opt6").value

                    //Getting option votes from db
                    val pollVoteOpt1 = poll.child("opt1Vote").value
                    val pollVoteOpt2 = poll.child("opt2Vote").value
                    val pollVoteOpt3 = poll.child("opt3Vote").value
                    val pollVoteOpt4 = poll.child("opt4Vote").value
                    val pollVoteOpt5 = poll.child("opt5Vote").value
                    val pollVoteOpt6 = poll.child("opt6Vote").value
                    val pollT = poll.child("poll_TITLE").value
                    //Getting poll ID from db
                    var pollId = poll.child("poll_ID").value
                    //Getting poster ID from db
                    val pollPosterId = poll.child("poster_ID").value
                    //Getting poll end time from db
                    var pollEndTime = poll.child("poll_Time").value

                    //Adding poll to recycler view
                    pollViews.add(pollT.toString())
                    //Log.w("NOT SURE: ", pollId.toString())
                }
            }
        }
        adapter?.notifyDataSetChanged()
        return v
    }
    private fun replaceChildFragment(childFragment : Fragment) {
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(R.id.activities_view, childFragment).addToBackStack(null).commit()
    }
}
