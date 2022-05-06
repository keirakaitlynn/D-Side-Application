package com.example.dsideapp.data
import java.util.*

data class ActivityObject (
    var id: String? = null,
    var title: String? = null,
    var phone_contact: String? = null,
    var image_type: String? = null,
    var location: LocationObject? = null,
    var business_name: String? = null,
    var price: String? = null,
    var category: String? = null) {

    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.

    // The parameters are the ame as EventObject's.
    fun toEvent(eventTitle : String?, startTime : Date?, endTime : Date?,
                                eventUsers : MutableList<String> ) =  EventObject(
        event_title =  eventTitle,
        start_time = startTime,
        end_time =  endTime,
        activity =  this,
        users = eventUsers
    )
}

