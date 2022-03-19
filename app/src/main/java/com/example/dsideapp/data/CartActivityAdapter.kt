package com.example.dsideapp.data

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.example.dsideapp.R
import com.google.firebase.database.DataSnapshot

// MMMMM: RecyclerView + CardView (ActivitiesFragment, CartPopUpFragment)
class CartActivityAdapter(val context: Context, private val cart: MutableList<DataSnapshot>)
    : DragDropSwipeAdapter<DataSnapshot, CartActivityAdapter.ViewHolder>(cart){

    private var activities: MutableList<DataSnapshot> = this.cart as MutableList<DataSnapshot>

    class ViewHolder(itemView: View): DragDropSwipeAdapter.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView
        var dragIcon: ImageView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
            dragIcon = itemView.findViewById(R.id.drag_handle)
        }
    }

    override fun getViewHolder(itemView: View) = CartActivityAdapter.ViewHolder(itemView)

    // MMMMM: Required for DragDropSwipeAdapter.
    // XXXXX: Calling onBindViewHolder(viewHolder, position) vs. this
    override fun onBindViewHolder(item: DataSnapshot, viewHolder: CartActivityAdapter.ViewHolder, position: Int) {
        viewHolder.itemTitle.text = item.child("title").value.toString()
        viewHolder.itemDetail.text = item.child("title").value.toString()
    }

    override fun getViewToTouchToStartDraggingItem(
        item: DataSnapshot,
        viewHolder: CartActivityAdapter.ViewHolder,
        position: Int
    ): View? {
        return viewHolder.dragIcon
    }

    override fun onDragFinished(item: DataSnapshot, viewHolder: CartActivityAdapter.ViewHolder) {
        Log.d("DragDropAdapter", "$cart")
    }

    fun updateItem(item: DataSnapshot) {
        cart.add(0, item)
        notifyItemInserted(0)
        Log.d("DragDropAdapter", "$activities")
    }

    override fun onBindViewHolder(holder: CartActivityAdapter.ViewHolder, position: Int) {
        // Iterates through an array (cart, images, etc).
        // Binds ViewHolder's vars to RecyclerAdapter's vars (cart_activity_card.xml w/ CartActivityAdapter.kt).
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
    }


}
