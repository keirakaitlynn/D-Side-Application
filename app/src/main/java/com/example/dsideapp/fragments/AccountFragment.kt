package com.example.dsideapp.fragments

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.childfragments.InformationChildFragment
import com.google.firebase.database.FirebaseDatabase

class AccountFragment : Fragment() {
    lateinit var infoButton : ImageButton
    private val infoFragment = InformationChildFragment()
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
        var image = 0
        db.child("users").child(userID.toString()).child("pfp").get().addOnSuccessListener {
            image = it.value.toString().toInt()
            Log.w("HERE : ", it.value.toString())
            Log.w(image.toString(), R.drawable.pikachupfp.toString())
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
        transaction.replace(R.id.activities_view, childFragment).addToBackStack(null).commit()
    }
}