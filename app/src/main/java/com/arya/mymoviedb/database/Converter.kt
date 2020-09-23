package com.arya.mymoviedb.database

import androidx.room.TypeConverter
import com.arya.mymoviedb.model.Cast
import com.arya.mymoviedb.model.Trailer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    @TypeConverter
    fun genreToJson(value: List<String>): String {
        val type = object: TypeToken<List<String>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun jsonToGenre(value: String): List<String> {
        val type = object: TypeToken<List<String>>() {}.type
        return  Gson().fromJson(value, type)
    }

    @TypeConverter
    fun castToJson(value: List<Cast>): String {
        val type = object: TypeToken<List<Cast>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun jsonToCast(value: String): List<Cast> {
        val type = object: TypeToken<List<Cast>>() {}.type
        return  Gson().fromJson(value, type)
    }

    @TypeConverter
    fun trailerToJson(value: List<Trailer>): String {
        val type = object: TypeToken<List<Trailer>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun jsonToTrailer(value: String): List<Trailer> {
        val type = object: TypeToken<List<Trailer>>() {}.type
        return  Gson().fromJson(value, type)
    }
}