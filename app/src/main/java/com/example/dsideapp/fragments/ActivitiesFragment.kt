package com.example.dsideapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.R

class ActivitiesFragment : Fragment() {
    lateinit var suggestionsButton : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_activities, container, false)

        suggestionsButton = v.findViewById<Button>(R.id.suggestionsButton)
        suggestionsButton.setOnClickListener{
            val suggestionsFragment = SuggestionsFragment()
            val transaction : FragmentTransaction? = fragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragment_view, suggestionsFragment).commit()
            }
        }
        return v
    }
}