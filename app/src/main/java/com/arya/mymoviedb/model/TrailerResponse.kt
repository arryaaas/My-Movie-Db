package com.arya.mymoviedb.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TrailerResponse(
    @SerializedName("results")
    val result: List<Trailer>
)

class Trailer (
    @SerializedName("key")
    var key: String? = null,

    @SerializedName("name")
    var name: String? = null
): Serializable
