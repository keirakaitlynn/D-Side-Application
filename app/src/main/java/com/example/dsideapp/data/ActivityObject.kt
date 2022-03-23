package com.example.dsideapp.data

data class ActivityObject (
    val id: String? = null,
    var title: String? = null,
    val phone_contact: String? = null,
    var image_type: String? = null,
    val location: LocationObject? = null,
    var business_name: String? = null,
    val price: String? = null,
    //val categories: YelpCategoryClass? = null
    ) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}