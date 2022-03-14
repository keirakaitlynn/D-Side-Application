package com.example.dsideapp.data

import java.util.*

data class PollObject (
    val id: String? = null,
    val poll_poster_id: String? = null,
    val poll_options: MutableList<String>? = null,
    val poll_vote_count: MutableList<Int>? = null,
    val poll_end_time: Calendar? = null,
    val business_name: String? = null,
    val winner_index: Int? = null) {
    fun calculate_percentages(): //MutableList<Double>
    //When calculating percentages, replace Boolean with commented out MutableList<Double> and return the resulting list of percentages instead.
    Boolean {
        return true
    }
    fun send_results(): Boolean {
        return true
    }
}