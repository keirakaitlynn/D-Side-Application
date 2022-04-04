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

    private val teamFragment = TeamChildFragment()
    private val FAQFragment = FAQChildFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_information_child, container, false)

        teamButton = v.findViewById<Button>(R.id.ToTeamPage)
        teamButton.setOnClickListener{
            replaceChildFragment(teamFragment)
        }

        FAQButton = v.findViewById<Button>(R.id.ToFAQPage)
        FAQButton.setOnClickListener{
            replaceChildFragment(FAQFragment)
        }

        return v
    }

    private fun replaceChildFragment(childFragment : Fragment) {
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(R.id.informationFragment_View, childFragment).addToBackStack(null).commit()
    }
}