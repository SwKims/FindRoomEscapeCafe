package com.ksw.findroomescapecafe.api

import com.ksw.findroomescapecafe.model.RoomsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by KSW on 2021-09-08
 */

interface RoomService {
    @GET("/v5/api/search?=caller=")
    fun getRoomCafeList(
        @Query("query") query: String,
        @Query("displayCount") displayCount: Int = 15
    ) : Call<RoomsResponse>
}