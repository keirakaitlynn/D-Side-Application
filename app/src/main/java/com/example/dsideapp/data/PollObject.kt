package com.example.dsideapp.data

import java.util.*
import kotlin.math.max

data class PollObject (
    val id: String? = null,
    val poll_poster_id: String? = null,
    val poll_options: MutableList<String>? = null,
    val poll_vote_count: MutableList<Int>? = null,
    val poll_end_time: Long? = null,
    val business_name: String? = null,
    var winner_index: Int? = null,
    var voters: String? = null) {
    /**
     * Uses the current poll vote counts to make a list of percents for their votes.
     * If end time is passed, then set winner_index.
     * Edge Cases:
     *  If winner_index is already set, skip functionality.
     *  If no votes in the poll, skip functionality.
     * @return
     */
    fun calc_perc_n_lock():
    MutableList<Double> {
        val curr_votes = this.poll_vote_count!!.sum()
        val to_return = mutableListOf<Double>()
        if (winner_index != null) {
            if (curr_votes != 0) {
                this.poll_vote_count.forEach {
                    to_return.add(((it / curr_votes) * 100).toDouble())
                }
            } else {
                System.err.println("There are no votes in this poll. " +
                            "No calculations are made and function returns empty MutableList")
            }
            if (Calendar.getInstance().after(poll_end_time)){
                winner_index = to_return.indexOf(to_return.maxOrNull() ?: Integer.MAX_VALUE)
            }
        }
        else {
            System.err.println("Winner is already decided. " +
                    "No calculations are made and function returns empty MutableList")
        }
        return to_return
    }
}