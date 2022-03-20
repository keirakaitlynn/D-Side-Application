package com.example.dsideapp.data

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.dsideapp.R

// MMMMM: SuggestionAdapter to replace ArrayAdapter used for SwipeFlingAdapterView
class SuggestionAdapter(context: Context, layout: Int, layoutItem: Int, restaurants: List<String>) :
    ArrayAdapter<String?>(context, layout, layoutItem, restaurants) {

    private val suggestions: List<String>
    private val mLayout: Int
    private val mItemLayout: Int
    private val mContext : Context

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    internal class ViewHolder {
        var activityTitle: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        var itemView: View? = convertView

        if (itemView == null) {
            val layoutInflater: LayoutInflater = (mContext as Activity).getLayoutInflater()
            itemView = layoutInflater.inflate(mItemLayout, null, true)
            viewHolder = ViewHolder()
            viewHolder.activityTitle = itemView.findViewById(R.id.activity_title) as TextView
            itemView.tag = viewHolder
        }
        else {
            viewHolder = itemView.tag as ViewHolder
        }

        val item: String = suggestions[position]
        viewHolder.activityTitle?.text = item

        return itemView!!
    }

    init {
        this.mContext = context
        this.suggestions = restaurants
        mLayout = layout
        mItemLayout = layoutItem
    }
}