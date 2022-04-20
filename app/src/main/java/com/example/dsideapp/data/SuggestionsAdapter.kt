package com.example.dsideapp.data

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SuggestionsAdapter(context: Context?, strings: MutableList<YelpRestaurant>) :
    ArrayAdapter<YelpRestaurant?>(context!!, 0, strings as List<YelpRestaurant?>) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView: View? = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(com.example.dsideapp.R.layout.activity_card, parent, false);
        }

        val restaurant = getItem(position)

        val activityTitle = convertView?.findViewById(com.example.dsideapp.R.id.activity_title) as TextView
        val activityCategory = convertView.findViewById(com.example.dsideapp.R.id.activity_category) as TextView
        val activityRadius = convertView.findViewById(com.example.dsideapp.R.id.activity_radius) as TextView
        val activityRating = convertView.findViewById(com.example.dsideapp.R.id.activity_rating) as TextView
        val firstCategory = restaurant?.categories?.get(0)
        activityTitle.setText(restaurant?.name)
        activityCategory.setText(firstCategory?.title)
        activityRadius.setText(restaurant?.displayDistance())
        activityRating.setText("⭐" + restaurant?.rating.toString())

        return convertView
    }
}