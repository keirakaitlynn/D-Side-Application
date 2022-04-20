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

var selectedItemsForDecisionTools = mutableMapOf<DataSnapshot, Int>()

// MMMMM: RecyclerView + CardView (ActivitiesFragment, CartPopUpFragment)
class CartActivityAdapter(val context: Context, val cart: MutableList<DataSnapshot>): RecyclerView.Adapter<CartActivityAdapter.ViewHolder>() {

    // NOTES: size of arrays of info to bind w/ ViewHolder must be equal
    private val selectedItemsForDecisionTools = mutableMapOf<DataSnapshot, Int>()
    private var maxTiles = cart.size // max # of tiles user can place is = to the initial size of cart
    val numImages = intArrayOf(R.drawable.ic_baseline_filter_1_24, R.drawable.ic_baseline_filter_2_24, R.drawable.ic_baseline_filter_3_24, R.drawable.ic_baseline_filter_4_24, R.drawable.ic_baseline_filter_5_24, R.drawable.ic_baseline_filter_6_24, R.drawable.ic_baseline_filter_7_24, R.drawable.ic_baseline_filter_8_24, R.drawable.ic_baseline_filter_9_24, R.drawable.ic_baseline_10k_24)
    var possibleNumsToAssign = mutableListOf<Int>(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)

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

        maxTiles = cart.size
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
                Log.d("selectedItems", "${selectedItemsForDecisionTools} BEFORE")
                Log.d("maxTiles", "${maxTiles} BEFORE")
                // MMMMM: onClick & onSecondClick functionality
                // XXXXX: Based on maxTiles, add a # icon to selected cartActivity
                // NNNNN: As long as the user still has tiles remaining to be assigned...
                // NNNNN: If selected cartActivity has not already been selected...
                if (maxTiles != 0 && !possibleNumsToAssign.isNullOrEmpty() && !selectedItemsForDecisionTools.contains(activity)) {
                    Log.d("Nums", "${possibleNumsToAssign}")
                    val numToAssign = possibleNumsToAssign[possibleNumsToAssign.size-1] // NNNNN: get the last # in nums
                    selectedItemsForDecisionTools.put(activity, numToAssign) // NNNNN: assign that # to selected cartActivity
                    holder.itemImage.setImageResource(numImages[numToAssign-1]) // NNNNN: assign itemImage to corresponding icon (ie. if numToAssign=1, get numImages[0])
                    possibleNumsToAssign.remove(numToAssign) // NNNNN: "pop" off the # from nums
                    maxTiles -= 1
                }
                else { // NNNNN: If selected cartActivity has already been selected...
                    // NNNNN: put back DataSnapshot's assigned # in nums
                    possibleNumsToAssign.add(selectedItemsForDecisionTools.get(activity)!!)
                    possibleNumsToAssign.sortDescending()
                    holder.itemImage.setImageResource(R.color.purple_100)
                    Log.d("Nums AFTER PUT BACK", "${possibleNumsToAssign}")
                    selectedItemsForDecisionTools.remove(activity)
                    maxTiles += 1
                }
                Log.d("selectedItems", "${selectedItemsForDecisionTools} AFTER")
                Log.d("maxTiles", "${maxTiles} AFTER")
                Log.d("Nums", "${possibleNumsToAssign}")

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

    fun deleteItem(cartActivity: DataSnapshot) {
        cart.remove(cartActivity)
        maxTiles -= 1 // decrease max # of tiles user can place by 1 each time user deletes a cartActivity
        selectedItemsForDecisionTools.remove(cartActivity) // if cartActivity is removed from database, remove from selectedItemsForDecisionTools too
        notifyDataSetChanged()
    }

    fun addItem(i : Int, cartActivity : DataSnapshot) {
        cart.add(i, cartActivity) // add cartActivity to position i in cart list
        notifyDataSetChanged()
    }

}
