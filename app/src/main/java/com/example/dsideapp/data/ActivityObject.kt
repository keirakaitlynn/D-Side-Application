package com.example.dsideapp.data

data class ActivityObject (
    val id: String? = null,
    val title: String? = null,
    val phone_contact: String? = null,
    val image_type: String? = null,
    val location: LocationObject? = null,
    val business_name: String? = null,
    val price: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}