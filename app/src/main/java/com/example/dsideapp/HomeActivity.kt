package com.example.dsideapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.dsideapp.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val activitiesFragment = ActivitiesFragment()
    private val pollsFragment = PollsFragment()
    private val calendarFragment = CalendarFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        replaceFragment(homeFragment)
        //supportFragmentManager.beginTransaction().replace(R.id.fragment_view, homeFragment).commit()

        val navBar = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        navBar.setOnItemSelectedListener() {
            when(it.itemId) {
                R.id.navbar_home -> replaceFragment(homeFragment)
                R.id.navbar_activities -> replaceFragment(activitiesFragment)
                R.id.navbar_polls -> replaceFragment(pollsFragment)
                R.id.navbar_calendar -> replaceFragment(calendarFragment)
                //REPLACE NAVBAR_ACCOUNT WITH THIS WHEN WE GET TO ACCOUNT PAGE WORK -> accountFragment
                R.id.navbar_account -> replaceFragment(accountFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_view, fragment).commit()
            return true
        }
        return false
    }

    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.fragment_view)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }

    interface IOnBackPressed {
        fun onBackPressed(): Boolean
    }
}
