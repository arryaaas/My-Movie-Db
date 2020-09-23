package com.arya.mymoviedb.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CastResponse(
    @SerializedName("cast")
    val cast: List<Cast>
)

class Cast (
    @SerializedName("name")
    var name: String? = null,

    @SerializedName("profile_path")
    var profilePath: String? = null
): Serializable
