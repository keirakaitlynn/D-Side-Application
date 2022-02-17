package com.example.dsideapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.R
import com.example.dsideapp.childfragments.CoinChildFragment
import com.example.dsideapp.childfragments.DiceChildFragment
import com.example.dsideapp.childfragments.SuggestionsChildFragment
import com.example.dsideapp.childfragments.WheelChildFragment

class ActivitiesFragment : Fragment() {
    lateinit var suggestionsButton : Button
    lateinit var coinButton : Button
    lateinit var diceButton : Button
    lateinit var wheelButton : Button

    private val suggestionsFragment = SuggestionsChildFragment()
    private val coinFragment = CoinChildFragment()
    private val diceFragment = DiceChildFragment()
    private val wheelFragment = WheelChildFragment()
    private val concertsFragment = ConcertsFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_activities, container, false)
        replaceChildFragment(suggestionsFragment) // initial child fragment

        suggestionsButton = v.findViewById<Button>(R.id.suggestions_button)
        suggestionsButton.setOnClickListener{
            replaceChildFragment(suggestionsFragment)
        }
        coinButton = v.findViewById<Button>(R.id.coin_button)
        coinButton.setOnClickListener{
            replaceChildFragment(coinFragment)
        }
        diceButton = v.findViewById<Button>(R.id.dice_button)
        diceButton.setOnClickListener{
            replaceChildFragment(diceFragment)
        }
        wheelButton = v.findViewById<Button>(R.id.wheel_button)
        wheelButton.setOnClickListener{
            //Temporarily have this in concert fragment to avoid code clash
            replaceChildFragment(concertsFragment)

            //replaceChildFragment(wheelFragment)
        }

        return v
    }

    private fun replaceChildFragment(childFragment : Fragment) {
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(R.id.activities_view, childFragment).commit()
    }
}