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

class TeamChildFragment : Fragment() {

    val infoList = ArrayList<Expandable>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_team_child, container, false)

        // Initialize Data
        infoList.add(
            Expandable(
                title = "Keira Wong",
                description = "Hello there! I'm Keira and I am a fourth-year Computer Science student with plans to pursue a Masters in Graphic Design post-graduation. My goal is to work towards becoming a well-rounded, multi-disciplinary graphics designer with a strong foundation in programming so that I am able to collaborate with other creatives on large projects and handle a wide variety of tasks in the future."
            )
        )
        infoList.add(
            Expandable(
                title = "Estefania Lopez",
                description = "Hello! I'm Estefania, or Este for short, I'm currently a fourth year computer science student. With many areas of volunteerings in the engineering field, as well as  proficiencies in many langauges, I am to bring work and leadership."
            )
        )
        infoList.add(
            Expandable(
                title = "Joshua Vallido",
                description = "I am a fourth year at California State University of Long Beach working towards a Bachelor's degree in Computer Science. I am a hard worker who enjoys a dynamic environment and is interested in seeing all the opportunities the world has to offer."
            )
        )
        infoList.add(
            Expandable(
                title = "Victor Rodriguez",
                description = "Hey! I'm Victor Rodriguez. Just a humble student who loves computer science. Everyday is a new opportunity to learn."
            )
        )
        infoList.add(
            Expandable(
                title = "Sooyoung Kawasaki",
                description = "Hello! I'm Sooyoung. I'm a Comp Science major hoping to bring leadership from personal growth and volunteering through nonprofit organization and skills through experience and learning from my fellow colleagues."
            )
        )
        // Set up Recycler View
        val expandableCardAdapter = ExpandableCardAdapter(infoList)
        val expandableRecyclerView = v.findViewById<RecyclerView>(R.id.FAQRecyclerView)
        expandableRecyclerView.adapter = expandableCardAdapter
        expandableRecyclerView.setHasFixedSize(true)

        return v
    }
}