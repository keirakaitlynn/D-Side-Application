package com.example.dsideapp.data

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

// MMMMM: RecyclerView + CardView (ActivitiesFragment, CartPopUpFragment)
class CartActivityAdapter(val context: Context, val cart: MutableList<DataSnapshot>): RecyclerView.Adapter<CartActivityAdapter.ViewHolder>() {

    // NOTES: size of arrays of info to bind w/ ViewHolder must be equal
//    private var titles = arrayOf("Chapter One", "Chapter Two", "Chapter Three", "Chapter Five", "Chapter Six", "Chapter Seven", "Chapter Eight")
//    private var details = arrayOf("Item one details", "Item two details", "Item three details", "Item four details", "Item five details", "Item six details", "Item seven details", "Item eight details")
//    private var images = intArrayOf(R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartActivityAdapter.ViewHolder {
        // ViewHolder: the layout of the item to be duplicated/"recycled"
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cart_activity_card, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        // the total # of items you want to display to pass to ViewHolder
        return cart.size
    }

    override fun onBindViewHolder(holder: CartActivityAdapter.ViewHolder, position: Int) {
        // Iterates through an array (cart, images, etc).
        // Binds ViewHolder's vars to RecyclerAdapter's vars (cart_activity_card.xml w/ RecyclerAdapter.kt).

        // for each DataSnapshot (activity) in list of DataSnapshots (cart)
        val activity = cart[position]

        holder.itemTitle.text = activity.child("title").value.toString()

        var location = ""
        location += activity.child("location").child("city").value.toString()
        location += ", "
        location += activity.child("location").child("state").value.toString()
        holder.itemDetail.text = location

        // NOTES: size of arrays of info to bind w/ ViewHolder must be equal
        // NOTES: Line below will crash because images[].size != cart.size (or "getItemCount()")
        //holder.itemImage.setImageResource(images[position])
        holder.itemView.setOnClickListener {
            Log.d("Clicked", "${holder.itemsId} Selected")
        }

        holder.itemsId = cart[position].child("id").value.toString()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView
        var itemsId : String = ""

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
        }
    }

    // get ID of DataSnapshot located at cart[position]
    fun getItemsId(position: Int): String {
        val activityID = cart[position].child("id").value.toString()
        return activityID
    }

    fun deleteItem(i : Int) {
        cart.removeAt(i)
        notifyDataSetChanged()
    }

    fun addItem(i : Int, cartActivity : DataSnapshot) {
        cart.add(i, cartActivity) // add cartActivity to position i in cart list
        notifyDataSetChanged()
    }
}
