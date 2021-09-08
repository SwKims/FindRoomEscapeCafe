package com.ksw.findroomescapecafe.model

import com.google.gson.annotations.SerializedName

/**
 * Created by KSW on 2021-09-08
 */

data class RoomsResult(
    @SerializedName("place")
    var place : RoomsList
)
