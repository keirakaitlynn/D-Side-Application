package com.example.dsideapp.childfragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.data.FriendClass
import com.google.firebase.database.FirebaseDatabase


class AddFriendFragment : Fragment() {

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val v = inflater.inflate(R.layout.fragment_child_wheel, container, false)
//        return v
//    }

    private lateinit var viewOfLayout: View

    // Roughly how big each user value's is represented on the wheel.
    private lateinit var sectorDegrees: IntArray
    var calendar: CalendarView? = null
    private var dateView: TextView? = null

    //getting database info
    var authorization = auth
    var user = authorization.currentUser
    var userID = authorization.currentUser?.uid
    var db = FirebaseDatabase.getInstance().getReference()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewOfLayout = inflater.inflate(R.layout.fragment_add_friend, container, false)

        val y = viewOfLayout.findViewById<Button>(R.id.friendSearchButton)
        y.setOnClickListener{
            val x = viewOfLayout.findViewById<EditText>(R.id.friendEmailInput).text.toString().split("@")[0]
            val z  = db.child("Phonebook")
            z.child(x).get().addOnSuccessListener {
                Log.w(("Success reading"), "Got it ${it.value}")
                Log.w("Bruh", "${it.key}")
                val a = it.getValue() as HashMap<String?, String?>

                Log.w("Plz", "${a.get("email")}  and ${a.get("uid")}")
                val b = FriendClass(a.get("email"), a.get("uid"), a.get("userName"))
                db.child("users").child(auth.uid.toString()).child("data").child("Friends").child(x).setValue(b)
            }.addOnFailureListener{
                Log.w("Failure reading", it)
            }
        }

        return viewOfLayout

    }
}