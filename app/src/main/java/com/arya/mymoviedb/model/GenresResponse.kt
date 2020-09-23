package com.arya.mymoviedb.model

import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("genres")
    val genres: List<Genre>
)

class Genre (
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null
)