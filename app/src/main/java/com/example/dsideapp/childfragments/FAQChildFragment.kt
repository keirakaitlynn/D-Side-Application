package com.example.dsideapp.childfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.data.ExpandableCardAdapter
import com.example.dsideapp.data.Expandable

class FAQChildFragment : Fragment() {

    val infoList = ArrayList<Expandable>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_f_a_q_child, container, false)

        // Initialize Data
        infoList.add(Expandable(
            title = "Will my friends have to sign up to be added and notified about group events?",
            description = "All users will have to sign up to use any functionality in the app to tailor the experience to each individual user."
        ))
        infoList.add(Expandable(
            title = "Will I be able to add my own activities for the app to be used?",
            description = "Yes! Navigate to the cart to add dummy activities for any activity not found in the system."
        ))
        infoList.add(Expandable(
            title = "Will I be able to add my own activities for the app to be used?",
            description = "Yes! Navigate to the cart to add dummy activities for any activity not found in the system."
        ))
        infoList.add(Expandable(
            title = "What difference does this have to a random option picker online?",
            description = "Our app provides functionalities top of a random option picker, like a calendar and friends list, to ensure that the whole experience is as intuitive as possible."
        ))
        infoList.add(Expandable(
            title = "How can I add my friends to an event?",
            description = "You will need the email your friend used to invite them to any events."
        ))
        // Set up Recycler View
        val expandableCardAdapter = ExpandableCardAdapter(infoList)
        val expandableRecyclerView = v.findViewById<RecyclerView>(R.id.FAQRecyclerView)
        expandableRecyclerView.adapter = expandableCardAdapter
        expandableRecyclerView.setHasFixedSize(true)

        return v
    }

}