package com.example.dsideapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment

import android.view.Gravity

import android.widget.PopupWindow

import android.widget.LinearLayout

import android.annotation.SuppressLint

import android.view.View.OnTouchListener
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import android.content.Context.LAYOUT_INFLATER_SERVICE as LAYOUT_INFLATER_SERVICE1


class CalendarFragment : Fragment() {
    private lateinit var viewOfLayout: View
    var calendar: CalendarView? = null
    private var dateView: TextView? = null

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(com.example.dsideapp.R.layout.fragment_calendar, container, false)
        // Variables for easy manipulation of objects in the activity_main.xml file   🙂
        calendar = viewOfLayout.findViewById<View>(com.example.dsideapp.R.id.calendar) as CalendarView
        dateView = viewOfLayout.findViewById<View>(com.example.dsideapp.R.id.date_view) as TextView

        // Listener checks for a tap on a day

        calendar!!
            .setOnDateChangeListener {


                // and query the DB to actually get the event info 🙂

                _, year, month, dayOfMonth ->

                // Access and write the rewrite the date ontop of the sreen
                val Date = ("" + (month + 1) + "-"
                        + dayOfMonth.toString() + "-" + year)
                // set this date in TextView for Display
                dateView!!.text = Date

                // In here we can probably just make a popup / change page with a view of the day and stuff

                @SuppressLint("ClickableViewAccessibility")
                fun onButtonShowPopupWindowClick(view: View?) {

                    // inflate the layout of the popup window
                    val popupView: View? =
                        inflater?.inflate(com.example.dsideapp.R.layout.fragment_calendar, null)

                    // create the popup window
                    val width = LinearLayout.LayoutParams.WRAP_CONTENT
                    val height = LinearLayout.LayoutParams.WRAP_CONTENT
                    val focusable = true // lets taps outside the popup also dismiss it
                    val popupWindow = PopupWindow(popupView, width, height, focusable)

                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window tolken
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

                    // dismiss the popup window when touched
                    popupView?.setOnTouchListener { v, event ->
                        popupWindow.dismiss()
                        true
                    }
                }

            }

        return viewOfLayout
    }
}

