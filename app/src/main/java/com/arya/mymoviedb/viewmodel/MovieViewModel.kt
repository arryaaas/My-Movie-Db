package com.arya.mymoviedb.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.mymoviedb.model.Genre
import com.arya.mymoviedb.model.GenresResponse
import com.arya.mymoviedb.api.DataSource
import com.arya.mymoviedb.api.NetworkProvider
import com.arya.mymoviedb.model.MovieResponse
import com.arya.mymoviedb.model.MovieResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {
    private val movie = MutableLiveData<List<MovieResult>>()
    private val genre = MutableLiveData<List<Genre>>()
    private val error = MutableLiveData<Boolean>()

    fun setMovie() {
        val dataSource = NetworkProvider.providesHttpAdapter()
            .create(DataSource::class.java)
        dataSource.discoverMovie().enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e("MovieViewModel", "${t.printStackTrace()}")
                error.value = true
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                movie.value = response.body()?.result ?: emptyList()
            }

        })
    }

    fun setGenre() {
        val dataSource = NetworkProvider.providesHttpAdapter()
            .create(DataSource::class.java)
        dataSource.genreMovie().enqueue(object : Callback<GenresResponse> {
            override fun onFailure(call: Call<GenresResponse>, t: Throwable) {
                Log.e("MovieViewModel", "${t.printStackTrace()}")
            }

            override fun onResponse(
                call: Call<GenresResponse>,
                response: Response<GenresResponse>
            ) {
                genre.value = response.body()?.genres ?: emptyList()
            }

        })
    }

    fun getMovie(): LiveData<List<MovieResult>> {
        return movie
    }

    fun getGenre(): LiveData<List<Genre>> {
        return genre
    }

    fun getError(): LiveData<Boolean> {
        return error
    }
}