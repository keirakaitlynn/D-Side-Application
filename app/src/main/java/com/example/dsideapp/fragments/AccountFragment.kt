package com.example.dsideapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.LoginActivity
import com.example.dsideapp.R
import com.google.firebase.auth.FirebaseAuth
import com.example.dsideapp.auth
import com.example.dsideapp.data.CategoryAdapter
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class AccountFragment : Fragment() {
    lateinit var imageButton: ImageButton
    lateinit var textView: TextView
    private lateinit var viewOfLayout: View
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //TRY RUNNING AND SEE WHAT HAPPEND <------
        //DB info//
        var authorization = com.example.dsideapp.auth
        var user = authorization.currentUser
        var userID = authorization.currentUser?.uid
        var db = FirebaseDatabase.getInstance().getReference()

        viewOfLayout =
            inflater.inflate(R.layout.fragment_account, container, false)

        FirebaseAuth.getInstance()

        imageButton = viewOfLayout.findViewById<View>(R.id.log_out) as ImageButton
        //textView = findViewById(R.id.logged_in)

        //textView.setText("Welcome")

        imageButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            //Log.w("USER ID ", auth.uid.toString())
            val intent = Intent(activity, LoginActivity::class.java)
            activity?.startActivity(intent)
            //finish()
            //viewOfLayout = inflater.inflate(R.layout.activity_login, container, false)
            //startActivity(intent)

        }

        //// MMMMM: ====================================================================

        val searchButton = viewOfLayout.findViewById<Button>(R.id.favorite_search_button)
        searchButton.setOnClickListener{
            //// NNNNN: ====================================================================
            val searchView = viewOfLayout.findViewById<SearchView>(R.id.favorite_searchView)
            //val listView = v.findViewById<ListView>(R.id.listView)
            //val names = arrayOf("Android", "Java", "Php", "Python", "C", "C++", "Kotlin")

            var categories = ArrayList<String>()
            categories.add("Cake")
            categories.add("Cars")
            categories.add("Coffee & Tea")
            categories.add("Cookies")
            categories.add("Juice Bars & Smoothies")
            categories.add("Dance Clubs")
            categories.add("Dive Bars")
            categories.add("Dining")
            categories.add("Bowling")
            categories.add("Lounges")
            categories.add("Pizza")
            categories.add("Seafood")

            val categoryRecyclerView = viewOfLayout.findViewById<RecyclerView>(R.id.favorite_categoryRecyclerView)
            val categoryAdapter = CategoryAdapter(categories)
            categoryRecyclerView.setLayoutManager(LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))
            categoryRecyclerView.setAdapter(categoryAdapter)

            categoryRecyclerView.setNestedScrollingEnabled(false);

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    categoryAdapter.filter.filter(newText)
                    return false
                }

                override fun onQueryTextSubmit(s: String?): Boolean {
                    return false
                }
            })
            //// NNNNN: ====================================================================
        }

        //// MMMMM: ====================================================================

        var saveChangesButton = viewOfLayout.findViewById<Button>(R.id.changesSaveButton)
        saveChangesButton.setOnClickListener{
            db.child("users").child(userID.toString()).child("username")
                    .setValue(viewOfLayout.findViewById<EditText>(R.id.user_name).text.toString())
        }

        return viewOfLayout
    }
}