package com.example.dsideapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.dsideapp.fragments.*
import com.example.dsideapp.R
import com.example.dsideapp.childfragments.InformationChildFragment
import com.example.dsideapp.childfragments.eventSharingTesterFragment

class HomeFragment : Fragment() {
    private val activityTesterFragment = ActivityTesterFragment()
    private val informationTesterFragment = InformationChildFragment()
    private val eventSharingFragment = eventSharingTesterFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(com.example.dsideapp.R.layout.fragment_home, container, false)

        //Temporary activity tester button placement code
        val activityTesterButton = v.findViewById<Button>(com.example.dsideapp.R.id.activityTesterButton)
        activityTesterButton.setOnClickListener{
            val fragmentManager = getActivity()?.getSupportFragmentManager()
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view, informationTesterFragment).commit()
//                fragmentManager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view, activityTesterFragment).commit()
            }
        }
        return v
    }
}