package com.example.dsideapp.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class RestaurantsAdapter(val context: Context, val restaurants: List<YelpRestaurant>) :
    RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false))
    }

    override fun getItemCount() = restaurants.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(restaurant: YelpRestaurant) {
            itemView.findViewById<Button>(R.id.tvName).text = restaurant.name
            itemView.findViewById<Button>(R.id.tvName).setOnClickListener{
                val tempPhone = restaurant.phoneNum
                val tempTitle = restaurant.name
                val tempPrice = restaurant.price
                val tempImageURL = restaurant.imageUrl
                ///Save the cart info into the db temporarily
                //Creating db references
                var authorization = auth
                var user = authorization.currentUser
                var userID = authorization.currentUser?.uid
                var db = FirebaseDatabase.getInstance().getReference()
                //Creating writeToDB function
                fun writeNewActivity(userId: String, id: String, title: String = "None", phone: String = "None",
                                     image: String = "None", loc_address: String = "None", loc_city: String = "None",
                                     loc_country:String = "None", loc_zip: String = "None", loc_state: String = "None"
                                     , business_name: String = "None", price: String = "None") {
                    val location = LocationObject(loc_address, loc_city, loc_country, loc_zip, loc_state)
                    val activity = ActivityObject(if(id != "") id else "null", title, phone, image, location, business_name, price)
                    db.child("users").child(userId).child("data").child("cart").child(if(id != "") id else "null").setValue(activity)
                }
                    //Create random ID tag
                    var i = 0
                    var randID = ""
                    for(i in 1..3){
                        randID += Random.nextInt(9)
                    }
                    for(i in 1..3){
                        randID += (Random.nextInt(25) + 65).toChar()
                    }
                    //Actually saving activity to db
                    writeNewActivity(userId = userID.toString(), id = randID, title = tempTitle,
                        image = tempImageURL, business_name = tempTitle, price = tempPrice)

                ///
            }
        }
    }
}