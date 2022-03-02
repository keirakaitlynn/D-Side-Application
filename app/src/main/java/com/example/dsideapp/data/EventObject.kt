package com.example.dsideapp.data

import java.util.*

class EventObject (
    val start_time: Calendar? = null,
    val end_time: Calendar? = null,
    val activity: ActivityObject? = null,
    val users: ArrayList<String>? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
    //Date/Time | Calendar Class - This is something we already have and we definitely should keep to the end. https://developer.android.com/reference/kotlin/android/icu/util/Calendar
    //End Time | Calendar Class - This will be the end time (End Time - Date/Time = Duration)
    //Activity | Activity Class - This will be the Activity for the Event
    //Users | Array of AuthID - These will be participants of event. First participant is Owner
}