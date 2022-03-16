package com.example.dsideapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.data.ActivityObject
import com.example.dsideapp.data.LocationObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ActivityTesterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    lateinit var button: Button
    lateinit var textView: TextView

    lateinit var writeButton: Button
    lateinit var readButton: Button
    private lateinit var id: EditText
    private lateinit var title: EditText
    private lateinit var phone: EditText
    private lateinit var image: EditText
    private lateinit var loc_address: EditText
    private lateinit var loc_city: EditText
    private lateinit var loc_country: EditText
    private lateinit var loc_zip: EditText
    private lateinit var loc_state: EditText
    private lateinit var business_name: EditText
    private lateinit var price: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_tester_activity, container, false)

        auth = Firebase.auth
        val database = FirebaseDatabase.getInstance()

        id = v.findViewById<View>(R.id.activity_id) as EditText
        title = v.findViewById<View>(R.id.activity_title) as EditText
        phone = v.findViewById<View>(R.id.activity_phone) as EditText
        image = v.findViewById<View>(R.id.activity_image_upload) as EditText
        loc_address = v.findViewById<View>(R.id.location_address) as EditText
        loc_city = v.findViewById<View>(R.id.location_city) as EditText
        loc_country = v.findViewById<View>(R.id.location_country) as EditText
        loc_zip = v.findViewById<View>(R.id.location_zip) as EditText
        loc_state = v.findViewById<View>(R.id.location_state) as EditText
        business_name = v.findViewById<View>(R.id.activity_business_name) as EditText
        price = v.findViewById<View>(R.id.activity_price) as EditText

        fun writeNewActivity(userId: String, id: String, title: String, phone: String, image: String, loc_address: String, loc_city: String, loc_country:String, loc_zip: String, loc_state: String, business_name: String, price: String) {
            val location = LocationObject(loc_address, loc_city, loc_country, loc_zip, loc_state)
            val activity = ActivityObject(if(id != "") id else "null", title, phone, image, location, business_name, price)

            //For true functionality, set random list of characters to "userId" to properly write to currently logged in user. As well, set name in ".child(name)" as an ID in the future to make it easier to search and read from DB.
            database.reference.child("users").child(userId).child("data").child("activities").child(if(id != "") id else "null").setValue(activity)

        }

        writeButton = v.findViewById<Button>(R.id.write_button)
        writeButton.setOnClickListener {
            // Write a message to the database
            writeNewActivity(
                auth.uid.toString(),
                id.getText().toString(),
                title.getText().toString(),
                phone.getText().toString(),
                image.getText().toString(),
                loc_address.getText().toString(),
                loc_city.getText().toString(),
                loc_country.getText().toString(),
                loc_zip.getText().toString(),
                loc_state.getText().toString(),
                business_name.getText().toString(),
                price.getText().toString()
            )
        }

        readButton = v.findViewById<Button>(R.id.read_button)
        readButton.setOnClickListener {
            //-----------------------------
            //Given content in readId, print out activity found.
            val readId = id.getText().toString()
            //database.reference.child("users").child(auth.uid.toString()).child("data").child("cart").child("083GCW").child(readId).get().addOnSuccessListener {
            database.reference.child("users").child(auth.uid.toString()).child("data").child("activities").child(readId).get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }

            //-----------------------------
            //Iterate through every activity found in currrent user and print.
            //activities is the arrayList of ActivityObject read. You can iterate through to find each activity.
            /*
            val activities = arrayListOf<ActivityObject>()
            val ref = database.reference.child("users").child(auth.uid.toString()).child("data").child("activities")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (productSnapshot in dataSnapshot.children) {
                        val activity = productSnapshot.getValue(ActivityObject::class.java)
                        activities.add(activity!!)
                    }
                    for (activityItr in activities) {
                        System.out.println(activityItr)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    throw databaseError.toException()
                }
            })

             */
        }

        return v
    }
}