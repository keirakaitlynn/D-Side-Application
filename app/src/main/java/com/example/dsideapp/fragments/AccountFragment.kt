package com.example.dsideapp.fragments

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.childfragments.CoinChildFragment
import com.example.dsideapp.childfragments.FAQChildFragment
import com.example.dsideapp.childfragments.InformationChildFragment
import com.example.dsideapp.childfragments.TeamChildFragment
import com.google.firebase.database.FirebaseDatabase

class AccountFragment : Fragment() {
    lateinit var infoButton : ImageButton
    private val infoFragment = InformationChildFragment()

    lateinit var teamButton : Button
    lateinit var FAQButton : Button
    lateinit var BackButton : ImageButton

    private val teamFragment = TeamChildFragment()
    private val FAQFragment = FAQChildFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //DB info//
        var authorization = auth
        var user = authorization.currentUser
        var userID = authorization.currentUser?.uid
        var db = FirebaseDatabase.getInstance().getReference()

        var v = inflater.inflate(R.layout.fragment_account, container, false)
        var saveChangesButton = v.findViewById<Button>(R.id.changesSaveButton)
        saveChangesButton.setOnClickListener{
            db.child("users").child(userID.toString()).child("username")
                .setValue(v.findViewById<EditText>(R.id.user_name).text.toString())
        }
        infoButton = v.findViewById<ImageButton>(R.id.info_button)
        infoButton.setOnClickListener{
            //SHOW POPUP
            // inflate the layout of the popup window
            v = inflater.inflate(com.example.dsideapp.R.layout.fragment_information_child, null)
            // create the popup window
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.MATCH_PARENT
            val focusable = false // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(v, width, height, focusable)

            //Popup window for the event name and buttons
            teamButton = v.findViewById<Button>(R.id.ToTeamPage)
            teamButton.setOnClickListener{
                replaceChildFragment(teamFragment)
            }

            FAQButton = v.findViewById<Button>(R.id.ToFAQPage)
            FAQButton.setOnClickListener{
                replaceChildFragment(FAQFragment)
            }

            BackButton = v.findViewById<ImageButton>(R.id.ToAccountPage)
            BackButton.setOnClickListener{
                popupWindow.dismiss()
            }
            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        }
        var image = 0
        db.child("users").child(userID.toString()).child("pfp").get().addOnSuccessListener {
            image = it.value.toString().toInt()
            Log.w("HERE : ", it.value.toString())

            var pfpImage = v.findViewById<ImageButton>(R.id.accountPFPInAccount)
            if (image == R.id.Turtle) {
                pfpImage.setImageResource(R.drawable.turtlepfp)
            } else if (image == R.id.Pikachu) {
                pfpImage.setImageResource(R.drawable.pikachupfp)
            } else if (image == R.id.Avatar) {
                pfpImage.setImageResource(R.drawable.avatarpfp)
            } else if (image == R.id.Stitch) {
                pfpImage.setImageResource(R.drawable.stitchpfp)
            } else if (image == R.id.Raze) {
                pfpImage.setImageResource(R.drawable.razepfp)
            } else if (image == R.id.Ponyo) {
                pfpImage.setImageResource(R.drawable.ponyopfp)
            }
        }

        return v
    }
    private fun replaceChildFragment(childFragment : Fragment) {
        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
        transaction.replace(R.id.TextInfoFragment_View, childFragment).addToBackStack(null).commit()
    }
}