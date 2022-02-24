package com.example.dsideapp.childfragments

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import android.widget.ImageView
import java.util.*


class DiceChildFragment : Fragment() {

    private var imageViewDice: ImageView? = null
    private val rng = Random()
    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater.inflate(R.layout.fragment_child_dice, container, false)

        var rollButton: Button = viewOfLayout.findViewById(R.id.Button) as Button
        //super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_child_dice)
        //imageViewDice = view?.findViewById(R.id.image_view_dice)
        imageViewDice = viewOfLayout.findViewById(R.id.image_view_dice) as ImageView?

        rollButton.setOnClickListener(View.OnClickListener { rollDice() })

        Log.w("Number Generated", ""+rng)

        return  viewOfLayout
    }

    private fun rollDice() {
        when (rng.nextInt(6) + 1) {
            1 -> imageViewDice?.setImageResource(R.drawable.dice1)
            2 -> imageViewDice?.setImageResource(R.drawable.dice2)
            3 -> imageViewDice?.setImageResource(R.drawable.dice3)
            4 -> imageViewDice?.setImageResource(R.drawable.dice4)
            5 -> imageViewDice?.setImageResource(R.drawable.dice5)
            6 -> imageViewDice?.setImageResource(R.drawable.dice6)
        }
    }
}