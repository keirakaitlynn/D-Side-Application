package com.example.dsideapp.data
import java.util.*

data class ActivityObject (
    val id: String? = null,
    var title: String? = null,
    val phone_contact: String? = null,
    var image_type: String? = null,
    val location: LocationObject? = null,
    var business_name: String? = null,
    val categoryType : String? = null,
    val price: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.

    // The parameters are the ame as EventObject's.
    fun ActivityObject.toEvent(eventTitle : String?, startTime : Calendar?, endTime : Calendar?,
                                eventUsers : MutableList<String>, thisChecked : Boolean ) =  EventObject(
        event_title =  eventTitle,
        start_time = startTime,
        end_time =  endTime,
        activity =  this,
        users = eventUsers,
        checked = thisChecked
    )
}

