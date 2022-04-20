package com.example.dsideapp.data

import com.google.gson.annotations.SerializedName


data class YelpCategoryClass (
//      Each of these categories are used to turn our JSON Yelp info into classes.
    @SerializedName("alias"             ) var alias            : String?           = null,
    @SerializedName("title"             ) var title            : String?           = null,
    @SerializedName("parents"           ) var parents          : ArrayList<String> = arrayListOf(),
    @SerializedName("country_whitelist" ) var countryWhitelist : ArrayList<String>? = arrayListOf(),
    @SerializedName("country_blacklist") var countryBlackList: ArrayList<String>? = arrayListOf()
)
