package com.example.dsideapp.data

data class LocationObject(
    val address: String? = null,
    val city: String? = null,
    val country: String? = null,
    val zip: String? = null,
    val state: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
    }