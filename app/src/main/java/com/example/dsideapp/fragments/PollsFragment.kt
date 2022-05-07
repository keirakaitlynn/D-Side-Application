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
//
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
        var pollWatcher = pollAddEventListener()
        db.addChildEventListener(pollWatcher)
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
                popupWindow.dismiss()
                true
                ////MIGHT NOT NEED
                val fragmentManager = activity?.getSupportFragmentManager()
                Log.w("IDK WHY YOU ARE HERE: ",fragmentManager.toString())
                if (fragmentManager != null) {
                    fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view,  PollsFragment()).commit()
                    Log.w("Made it here","!")
                    //pleaseWorkManager = fragmentManager
                }
                //Log.w("Twas null","!")
                ////MIGHT NOT NEED
            }
        }
        ////////
        //Viewing your past polls
        var viewPast = v.findViewById<Button>(R.id.viewYourPollsButton)
        viewPast.setOnClickListener{
            val fragmentManager = activity?.getSupportFragmentManager()
            Log.w("IDK WHY YOU ARE HERE: ",fragmentManager.toString())
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view,  PastPollsFragment()).commit()
                Log.w("Made it here","!")
                //pleaseWorkManager = fragmentManager
            }
        }
        ////////
        //-----------------------------
        //Iterate through every currently active poll
        var pollInfo = db.get().addOnSuccessListener {
            if (it.exists()) {
                // for loop going through all the active polls
                val allPolls = it.children
                val rv = v.findViewById<RecyclerView>(R.id.pollsRecyclerView)
                val pollViews = mutableListOf<PollObject>()
                layoutManager = LinearLayoutManager(requireContext())
                rv.layoutManager = layoutManager
                adapter = RecyclerAdapterForTitles(requireContext(), pollViews)
                rv.adapter = adapter
                allPolls.forEach { poll ->
                    //if poll hasn't ended.
                    if (poll.child("poll_Time").value.toString().toLong() > Date().getTime()) {
                        //Getting poll options from db
                        val pollOpt1 = poll.child("opt1").value.toString()
                        val pollOpt2 = poll.child("opt2").value.toString()
                        val pollOpt3 = poll.child("opt3").value.toString()
                        val pollOpt4 = poll.child("opt4").value.toString()
                        val pollOpt5 = poll.child("opt5").value.toString()
                        val pollOpt6 = poll.child("opt6").value.toString()
                        //Creating a String List of Options to put in poll object
                        var tempOptionsHolder = mutableListOf<String>()
                        tempOptionsHolder.add(pollOpt1)
                        tempOptionsHolder.add(pollOpt2)
                        tempOptionsHolder.add(pollOpt3)
                        tempOptionsHolder.add(pollOpt4)
                        tempOptionsHolder.add(pollOpt5)
                        tempOptionsHolder.add(pollOpt6)

                        //Getting option votes from db
                        val pollVoteOpt1 = poll.child("opt1Vote").value.toString().toInt()
                        val pollVoteOpt2 = poll.child("opt2Vote").value.toString().toInt()
                        val pollVoteOpt3 = poll.child("opt3Vote").value.toString().toInt()
                        val pollVoteOpt4 = poll.child("opt4Vote").value.toString().toInt()
                        val pollVoteOpt5 = poll.child("opt5Vote").value.toString().toInt()
                        val pollVoteOpt6 = poll.child("opt6Vote").value.toString().toInt()
                        //Creating a Int List of Votes to put in poll object
                        var tempVoteHolder = mutableListOf<Int>()
                        tempVoteHolder.add(pollVoteOpt1)
                        tempVoteHolder.add(pollVoteOpt2)
                        tempVoteHolder.add(pollVoteOpt3)
                        tempVoteHolder.add(pollVoteOpt4)
                        tempVoteHolder.add(pollVoteOpt5)
                        tempVoteHolder.add(pollVoteOpt6)


                        val pollT = poll.child("poll_TITLE").value.toString()
                        //Getting poll ID from db
                        var pollId = poll.child("poll_ID").value.toString()
                        //Getting poster ID from db
                        val pollPosterId = poll.child("poster_ID").value.toString()
                        //Getting poll end time from db
                        var pollEndTime = poll.child("poll_Time").value.toString().toLong()
                        //Getting poll name
                        var pollName = poll.child("poll_TITLE").value.toString()
                        //getting voters
                        var allVoters = poll.child("voters").value.toString()

                        //Creating the poll object
                        var newPoll = PollObject(
                            pollId,
                            pollPosterId,
                            tempOptionsHolder,
                            tempVoteHolder,
                            pollEndTime,
                            pollName,
                            winner_index = 0,
                            allVoters
                        )
                        Log.w("VOTES: ", newPoll.poll_vote_count.toString())
                        Log.w("VOTES: ", newPoll.calc_perc_n_lock().toString())

                        //Adding poll to recycler view
                        //put in the poll object instead of only the title
                        pollViews.add(newPoll)
                        //Log.w("NOT SURE: ", pollId.toString())
                    } else {
                        //Add to user's "poll_results"
                        var userdb = FirebaseDatabase.getInstance().getReference("users")
                        userdb.child(poll.child("poster_ID").value.toString()).child("data")
                            .child("poll_results").child(poll.child("poll_ID").value.toString())
                            .setValue(poll.value)
                        //Delete poll
                        db.child(poll.child("poll_ID").value.toString()).setValue(null)
                    }
                }
            }
            adapter?.notifyDataSetChanged()

        }
        return v
    }
    private fun replaceChildFragment(childFragment : Fragment) {
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(R.id.activities_view, childFragment).addToBackStack(null).commit()
    }
    private class pollAddEventListener : ChildEventListener, Fragment() {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.w("Addition was detected","!")
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.w("Change was detected","!")
            //replaceChildFragment(this)
        //val fragmentManager = getActivity()?.getSupportFragmentManager()
            //Log.w("OnChildChanged: ",pleaseWorkManager.toString())
            /*if (pleaseWorkManager != null) {
                //og.w("---FRAG---", activity.toString())
                pleaseWorkManager?.beginTransaction()?.replace(com.example.dsideapp.R.id.fragment_view,  PollsFragment())?.commit()
            }*/
            //val fragmentManager = activity?.getSupportFragmentManager()
            //if (fragmentManager != null) {
           //     Log.w("Change was detected","!")
           //     fragmentManager.beginTransaction().detach(this).attach(this).commit()
           //     //fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view,  PollsFragment()).commit()
           // }
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
        //replaceChildFragment(PollsFragment())
            Log.w("Removal was detected","!")

            /*val fragmentManager = getActivity()?.getSupportFragmentManager()
                if (fragmentManager != null) {
                    fragmentManager.beginTransaction().detach(this).attach(this).commit()
                }*/
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            Log.w("Movement was detected","!")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("Cancellation detected","!")
        }

    }
}
