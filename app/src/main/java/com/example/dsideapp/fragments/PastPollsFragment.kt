package com.example.dsideapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.data.PollObject
import com.example.dsideapp.data.RecyclerAdapterForTitles
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class PastPollsFragment : Fragment() {
    private var adapter: RecyclerView.Adapter<RecyclerAdapterForTitles.ViewHolder>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_past_polls, container, false)
        //Getting poster's ID
        var authorization = auth
        var user = authorization.currentUser
        var userID = authorization.currentUser?.uid
        var db = FirebaseDatabase.getInstance().getReference("users").child(userID.toString())
            .child("data").child("poll_results")

        var pollInfo = db.get().addOnSuccessListener {
            if (it.exists()) {
                // for loop going through all the active polls
                val allPolls = it.children
                val rv = v.findViewById<RecyclerView>(R.id.pastpollsRecyclerView)
                val pollViews = mutableListOf<PollObject>()
                layoutManager = LinearLayoutManager(requireContext())
                rv.layoutManager = layoutManager
                adapter = RecyclerAdapterForTitles(requireContext(), pollViews)
                rv.adapter = adapter
                allPolls.forEach { poll ->
                //if poll hasn't ended.
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
                }
            }
            adapter?.notifyDataSetChanged()

        }

        return v
    }
}