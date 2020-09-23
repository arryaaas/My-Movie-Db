package com.arya.mymoviedb.api

import com.arya.mymoviedb.BuildConfig
import com.arya.mymoviedb.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DataSource {

    @GET("/3/discover/movie")
    fun discoverMovie(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY
    ): Call<MovieResponse>

    @GET("/3/discover/tv")
    fun discoverTv(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY
    ): Call<TvResponse>

    @GET("3/search/movie")
    fun searchMovie(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("query")
        queryKey: String
    ): Call<MovieResponse>

    @GET("3/search/tv")
    fun searchTv(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("query")
        queryKey: String
    ): Call<TvResponse>

    @GET("/3/discover/movie")
    fun releaseMovie(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
        @Query("primary_release_date.gte")
        gteKey: String,
        @Query("primary_release_date.lte")
        lteKey: String
    ): Call<MovieResponse>

    @GET("/3/genre/movie/list")
    fun genreMovie(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY
    ): Call<GenresResponse>
    
    @GET("/3/genre/tv/list")
    fun genreTv(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY
    ): Call<GenresResponse>

    @GET("/3/movie/{movie_id}/credits")
    fun getMovieCredits(
        @Path("movie_id")
        movieId: Int,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY
    ): Call<CastResponse>

    @GET("/3/tv/{tv_id}/credits")
    fun getTvCredits(
        @Path("tv_id")
        tvId: Int,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY
    ): Call<CastResponse>

    @GET("/3/movie/{movie_id}/videos")
    fun getMovieTrailer(
        @Path("movie_id")
        movieId: Int,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY
    ): Call<TrailerResponse>

    @GET("/3/tv/{tv_id}/videos")
    fun getTvTrailer(
        @Path("tv_id")
        movieId: Int,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY
    ): Call<TrailerResponse>
}