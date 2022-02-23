package com.example.dsideapp.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

public interface YelpService{

    @GET("businesses/search")
    fun searchRestaurants(
        // Required to send requests.
        @Header("Authorization") authHeader : String,

        // The thing you're searching for
        @Query("term") searchTerm: String?,         // The thing you're searching for

        // The radius (METERS)of the search from your location you typed in
        // Include a search of LATITUDE & LONGITUDE HERE
        @Query("radius") radius: Double?,

        // List of YELP Categories the user can go through :)
        // EX: Boating, Skiing, Hiking
        // Can look for two different things using OR via commas.
        // EX: "bars,french" is looking for French or BARS
        // Make sure the result is in the categories.
        // EX: "Disc golf" is a NO. But "discgolf" is a YES b/c of YELP.
        // Look here for the list of categories.
        // https://www.yelp.com/developers/documentation/v3/all_category_list
        @Query("categories") category: String?,

        // Sort results. Can sort by:
        // Best_Match (default if null), rating, review_count, distance
        @Query("sort_by") sort_by: String?,

        // Filter results by price. 1 = $??
        @Query("price") price: String?,

        // Filters based on whether or not its open in current time.
        // (DEFAULT IF NULL) false - doesn't matter if they're open right now.
        // Otherwise true - only returns restaurants that are open right now.
        @Query("open_now") openNow: Boolean?,

        // Filter based on a time the user inputs. Downside is that its an INT representing UNIX time.
        @Query("open_at") openAt: Int?,

        // Other things used to filter.
        // HOT_AND_NEW, reservation, deals (Yelp Deals), open_to_all
        @Query("attributes") attributes: String?,

        // callback will give us a yelpsearchResult OBJ for parsing into Objs :)
        @Query("location") location: String) : Call<YelpSearchResult>

}