package com.example.dsideapp.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.ArrayList

class CustomUsersAdapter(context: Context?, strings: ArrayList<String>?) :
    ArrayAdapter<String?>(context!!, 0, strings!! as List<String?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        // Check if an existing view is being reused, otherwise inflate the view
        var convertView: View? = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(com.example.dsideapp.R.layout.activity_card, parent, false);
        }

        // Get the data item for this position
        val user = getItem(position)

        // Lookup view for data population
        val activityTitle = convertView?.findViewById(com.example.dsideapp.R.id.activity_title) as TextView
        val activityLocation = convertView.findViewById(com.example.dsideapp.R.id.activity_location) as TextView
        // Populate the data into the template view using the data object
//        tvName.setText(user.getName())
//        tvHome.setText(user.getHometown())
        activityTitle.setText(user.toString())
        // Return the completed view to render on screen
        return convertView
    }
}