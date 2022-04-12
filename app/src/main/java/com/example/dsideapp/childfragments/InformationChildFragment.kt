package com.example.dsideapp.childfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.R

class InformationChildFragment : Fragment() {

    lateinit var teamButton : Button
    lateinit var FAQButton : Button
    lateinit var BackButton : ImageButton

    private val teamFragment = TeamChildFragment()
    private val FAQFragment = FAQChildFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_information_child, container, false)

        // create the popup window
        //Popup window for the event name and buttons
        teamButton = v.findViewById<Button>(R.id.ToTeamPage)
        teamButton.setOnClickListener{
            replaceChildFragment(teamFragment)
        }
        FAQButton = v.findViewById<Button>(R.id.ToFAQPage)
        FAQButton.setOnClickListener{
            replaceChildFragment(FAQFragment)
        }

        BackButton = v.findViewById<ImageButton>(R.id.ToAccountPage)
        BackButton.setOnClickListener{
            replaceParentFragment()
        }
        return v
    }
    private fun replaceChildFragment(childFragment : Fragment) {
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(R.id.TextInfoFragment_View, childFragment).addToBackStack(null).commit()
    }
    private fun replaceParentFragment() {
        val transaction: FragmentTransaction = getParentFragmentManager().beginTransaction()
        transaction.replace(R.id.info_view, Fragment()).addToBackStack(null).commit()
    }

}