package com.example.dsideapp.childfragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dsideapp.data.ActivityObject
import com.example.dsideapp.data.LocationObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

class EventAddPopUpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val v = inflater.inflate(R.layout.fragment_tester_activity, container, false)

        var v = inflater.inflate(R.layout.activity_dailyview, container, false)


        auth = Firebase.auth
        val database = FirebaseDatabase.getInstance()
        var eventButton: Button = v.findViewById<Button>(R.id.newEvent)

        eventButton.setOnClickListener{
            // inflate the layout of the popup window
            v = inflater.inflate(R.layout.fragment_tester_activity, null)
            Log.w("", "New Event Button is clicked")
            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(v, width, height, focusable)

//            //Popup window for the info
//            var infoPopUpText = v.findViewById<TextView>(R.id.popUpTextInfo)
//            val infoDescription = v.findViewById<TextView>(R.id.popUpTextInfo)
//            if (infoDescription != null) {
//                infoPopUpText.text = infoDescription.get(2).text().toString()
//            }
//            var infoImageView = v.findViewById<TextView>(R.id.popUpImageInfo)
//            //textView = findViewById(R.id.title)
//            val infoBitmap
//            infoImageView.setImageBitmap(infoBitmap)

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            v.setOnTouchListener { v, event ->
                popupWindow.dismiss()
                true
            }
        }

        return v
    }
}