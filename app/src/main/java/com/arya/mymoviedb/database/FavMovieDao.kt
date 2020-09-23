package com.arya.mymoviedb.database

import androidx.room.*
import com.arya.mymoviedb.model.MovieResult

@Dao
interface FavMovieDao {

    @Insert
    suspend fun saveMovie(favorite: MovieResult)

    @Query("select * from MovieResult")
    suspend fun readMovie(): List<MovieResult>

    @Delete
    suspend fun delMovie(favorite: MovieResult)

    @Query("select exists (select 1 from movieresult where id = :id)")
    suspend fun isFavoriteMovie(id: Int): Int
}