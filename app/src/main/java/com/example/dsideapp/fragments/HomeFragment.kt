package com.example.dsideapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.fragments.*
import com.example.dsideapp.R
import com.example.dsideapp.childfragments.InformationChildFragment


class HomeFragment : Fragment() {
    private val activityTesterFragment = ActivityTesterFragment()
    private val concertsFragment = PollsFragment()
    private val calendarFragment = CalendarFragment()
    private val accountFragment =AccountFragment()
    private val  activitiesFragment= ActivitiesFragment()
    private val  appInfoFragment= InformationChildFragment()
    lateinit var activitiesButton: ImageButton
    lateinit var calendarButton: ImageButton
    //change to polls in the future
    lateinit var concertsButton: ImageButton
    lateinit var infoButton: ImageButton
    lateinit var overallView : View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // val v = inflater.inflate(com.example.dsideapp.R.layout.fragment_home, container, false)


        var viewOfLayout = inflater.inflate(R.layout.fragment_home, container, false)
        overallView = viewOfLayout
        //FragamentTransaction fragmentTrans = getSupport

        //add activity button and transaction
        activitiesButton = viewOfLayout.findViewById<View>(R.id.activities_button) as ImageButton
        activitiesButton.setOnClickListener {
            val fragmentManager = getActivity()?.getSupportFragmentManager()
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view, ActivitiesFragment()).commit()
            }
        }
        //add calendar button and transaction
        calendarButton = viewOfLayout.findViewById<View>(R.id.calendar_button) as ImageButton
        calendarButton.setOnClickListener {
            val fragmentManager = getActivity()?.getSupportFragmentManager()
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view,  CalendarFragment()).commit()
            }
        }
        //add info button and transaction
        infoButton = viewOfLayout.findViewById<View>(R.id.information_button) as ImageButton
        infoButton.setOnClickListener {
            val fragmentManager = getActivity()?.getSupportFragmentManager()
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view, appInfoFragment).commit()
            }
        }

        //add concerts(polls) button and transaction
        concertsButton = viewOfLayout.findViewById<View>(R.id.concerts_button) as ImageButton
        concertsButton.setOnClickListener {
            val fragmentManager = getActivity()?.getSupportFragmentManager()
            Log.w("Home: ",fragmentManager.toString())
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
        return viewOfLayout
    }
}