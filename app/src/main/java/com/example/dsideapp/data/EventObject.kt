package com.example.dsideapp.data

import java.util.*

class EventObject (
    var event_title: String? = null,
    var start_time: Calendar? = null,
    var end_time: Calendar? = null,
    var activity: ActivityObject? = null,
    var users: MutableList<String>? = null,
    var checked: Boolean = false) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
    //Date/Time | Calendar Class - This is something we already have and we definitely should keep to the end. https://developer.android.com/reference/kotlin/android/icu/util/Calendar
    //End Time | Calendar Class - This will be the end time (End Time - Date/Time = Duration)
    //Activity | Activity Class - This will be the Activity for the Event
    //Users | Array of AuthID - These will be participants of event. First participant is Owner
    //Checked | Boolean - If the user already gave feedback on this event.
}