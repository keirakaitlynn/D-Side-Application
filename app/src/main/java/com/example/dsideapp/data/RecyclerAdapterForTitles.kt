package com.example.dsideapp.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class RecyclerAdapterForTitles(val context: Context, val titles: MutableList<String>): RecyclerView.Adapter<RecyclerAdapterForTitles.ViewHolder>() {

    // NOTES: size of arrays of info to bind w/ ViewHolder must be equal
//    private var titles = arrayOf("Chapter One", "Chapter Two", "Chapter Three", "Chapter Five", "Chapter Six", "Chapter Seven", "Chapter Eight")
//    private var details = arrayOf("Item one details", "Item two details", "Item three details", "Item four details", "Item five details", "Item six details", "Item seven details", "Item eight details")
//    private var images = intArrayOf(R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterForTitles.ViewHolder {
        // ViewHolder: the layout of the item to be duplicated/"recycled"
        val v = LayoutInflater.from(parent.context).inflate(R.layout.polls_view, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        // the total # of items you want to display to pass to ViewHolder
        return titles.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapterForTitles.ViewHolder, position: Int) {
        // Iterates through an array (cart, images, etc).
        // Binds ViewHolder's vars to RecyclerAdapter's vars (cart_activity_card.xml w/ RecyclerAdapter.kt).
        val title = titles[position]
        holder.itemTitle.text = title
        //holder.bind(title)

        // NOTES: size of arrays of info to bind w/ ViewHolder must be equal
        // NOTES: Line below will crash because images[].size != cart.size (or "getItemCount()")
        //holder.itemImage.setImageResource(images[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView

        /*fun bind(title: String) {
            itemView.findViewById<Button>(R.id.tvName).text = restaurant.name
            itemView.findViewById<Button>(R.id.tvName).setOnClickListener{//Voting and deleting here}
        }*/
        init {
            itemImage = itemView.findViewById(R.id.user_image)
            itemTitle = itemView.findViewById(R.id.poll_title)
        }
    }
}
