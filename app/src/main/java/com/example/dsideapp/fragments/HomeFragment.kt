package com.example.dsideapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.fragments.*
import com.example.dsideapp.R

class HomeFragment : Fragment() {
    private val activityTesterFragment = ActivityTesterFragment()
    private val concertsFragment = ConcertsFragment()
    private val calendarFragment = CalendarFragment()
    private val accountFragment =AccountFragment()
    private val  activitiesFragment= ActivitiesFragment()
    lateinit var activitiesButton: Button
    lateinit var calendarButton: Button
    //change to polls in the future
    lateinit var concertsButton: Button
    lateinit var infoButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(com.example.dsideapp.R.layout.fragment_home, container, false)

        //FragamentTransaction fragmentTrans = getSupport

        //add activity button and transaction
        activitiesButton = v.findViewById<Button>(R.id.suggestions_button)
        activitiesButton.setOnClickListener {
            val fragmentManager = getActivity()?.getSupportFragmentManager()
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view, ActivitiesFragment()).commit()
            }
        }
        //add calendar button and transaction
        calendarButton = v.findViewById<Button>(R.id.suggestions_button)
        calendarButton.setOnClickListener {
            val fragmentManager = getActivity()?.getSupportFragmentManager()
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view,  CalendarFragment()).commit()
            }
        }
        //add info button and transaction
        infoButton = v.findViewById<Button>(R.id.suggestions_button)
        infoButton.setOnClickListener {
            val fragmentManager = getActivity()?.getSupportFragmentManager()
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view, activityTesterFragment).commit()
            }
        }

        //add concerts(polls) button and transaction
        concertsButton = v.findViewById<Button>(R.id.suggestions_button)
        concertsButton.setOnClickListener {
            val fragmentManager = getActivity()?.getSupportFragmentManager()
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view,  ConcertsFragment()).commit()
            }
        }

//        //Temporary activity tester button placement code
//        val activityTesterButton = v.findViewById<Button>(com.example.dsideapp.R.id.activityTesterButton)
//        activityTesterButton.setOnClickListener{
//            val fragmentManager = getActivity()?.getSupportFragmentManager()
//            if (fragmentManager != null) {
//                fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view, activityTesterFragment).commit()
//            }
//        }
        return v
    }

    private fun replaceChildFragment(childFragment : Fragment) {
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(R.id.activities_view, childFragment).addToBackStack(null).commit()
    }
}