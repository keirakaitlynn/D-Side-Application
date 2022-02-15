package com.example.dsideapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.dsideapp.R
import kotlin.random.Random

class TestWheelFragment : Fragment() {

    lateinit var o1: String
    lateinit var o2: String
    lateinit var o3: String
    lateinit var o4: String

    lateinit var option1: EditText
    lateinit var option2: EditText
    lateinit var option3: EditText
    lateinit var option4: EditText
    lateinit var submit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Function to randomly choose on option between 1 and 4
        fun chooseActivity(): Int{
            val randomNum = Random.nextInt(1,5)
            return randomNum
        }
        val v = inflater.inflate(R.layout.fragment_activities, container, false)
        option1 = v.findViewById(R.id.option1)
        option2 = v.findViewById(R.id.option2)
        option3 = v.findViewById(R.id.option3)
        option4 = v.findViewById(R.id.option4)
        submit = v.findViewById<Button>(R.id.submit)
        submit.setOnClickListener{
            val chosenOption = chooseActivity()
            if (chosenOption==1){
                o1 = option1.text.toString()
            }
            else if(chosenOption==2){
                o2 = option2.text.toString()
            }
            else if(chosenOption==2){
                o3 = option2.text.toString()
            }
            else {
                o4 = option2.text.toString()
            }
        }
return v
    }
}