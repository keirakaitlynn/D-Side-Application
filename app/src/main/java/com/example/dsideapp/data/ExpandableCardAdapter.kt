package com.example.dsideapp.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R

class ExpandableCardAdapter(val infoList: MutableList<Expandable>) :
    RecyclerView.Adapter<ExpandableCardAdapter.ExpandableCardVM>() {
    class ExpandableCardVM(itemView: View) : RecyclerView.ViewHolder(itemView){

        var titleTxt : TextView = itemView.findViewById<TextView>(R.id.expandable_title)
        var descriptionTxt : TextView = itemView.findViewById<TextView>(R.id.expandable_desc)
        var linearLayout : LinearLayout = itemView.findViewById<LinearLayout>(R.id.linearLayout3)
        var expandableLayout : RelativeLayout = itemView.findViewById<RelativeLayout>(R.id.expandable_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableCardVM {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.expandable_card, parent, false)
        return ExpandableCardVM(view)
    }

    override fun onBindViewHolder(holder: ExpandableCardVM, position: Int) {

        val info : Expandable = infoList[position]
        holder.titleTxt.text = info.title
        holder.descriptionTxt.text = info.description

        val isExpandable : Boolean = infoList[position].isExpandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.linearLayout.setOnClickListener {
            val info = infoList[position]
            info.isExpandable = !info.isExpandable
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return infoList.size
    }
}