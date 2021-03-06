package com.example.dsideapp.data


import com.google.gson.annotations.SerializedName

//                          WE ARE USING THESE CLASSES TO PARSE THE DATA 🙂


// our top level JSON results
data class YelpSearchResult(
    @SerializedName("total") val total: Int,
    @SerializedName("businesses") val restaurants: List<YelpRestaurant>
)

data class YelpRestaurant(
    val name: String,
    val rating: Double,
    val price: String,
    @SerializedName("review_count") val numReviews: Int,
    @SerializedName("distance") val distanceInMeters: Double,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("phone") val phoneNum : String,
    val categories: List<YelpCategory>,


    // SerializedName is only used if the name of the param of the JSON file doesn't match your variable name
){
    fun displayDistance() : String {
        val milesPerMeter = 0.00062137
        val distanceInMiles = "%.2f".format(distanceInMeters * milesPerMeter)
        return "$distanceInMiles mi"
    }
}

data class YelpCategory(
    @SerializedName("title") val Title: String
)

data class YelpLocation(
    // Used to cast the info from the database into an obj. In this case it relates to the Yelp
    // Activity Location
    @SerializedName("address1") val address: String,
    @SerializedName("city") val cityInfo : String
)