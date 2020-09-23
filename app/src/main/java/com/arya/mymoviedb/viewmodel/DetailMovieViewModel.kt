package com.arya.mymoviedb.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.mymoviedb.api.DataSource
import com.arya.mymoviedb.api.NetworkProvider
import com.arya.mymoviedb.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel : ViewModel() {
    private val movieCast = MutableLiveData<List<Cast>>()
    private val movieTrailer = MutableLiveData<List<Trailer>>()

    fun setCast(movieId: Int) {
        val dataSource = NetworkProvider.providesHttpAdapter()
            .create(DataSource::class.java)
        dataSource.getMovieCredits(movieId).enqueue(object : Callback<CastResponse> {
            override fun onFailure(call: Call<CastResponse>, t: Throwable) {
                Log.e("CastMovieViewModel", "${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<CastResponse>, response: Response<CastResponse>) {
                movieCast.value = response.body()?.cast ?: emptyList()
            }

        })
    }

    fun setTrailer(movieId: Int) {
        val dataSource = NetworkProvider.providesHttpAdapter()
            .create(DataSource::class.java)
        dataSource.getMovieTrailer(movieId).enqueue(object : Callback<TrailerResponse> {
            override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                Log.e("TrailerMovieViewModel", "${t.printStackTrace()}")
            }

            override fun onResponse(
                call: Call<TrailerResponse>,
                response: Response<TrailerResponse>
            ) {
                movieTrailer.value = response.body()?.result ?: emptyList()
            }

        })
    }

    fun getCast(): LiveData<List<Cast>> {
        return movieCast
    }

    fun getTrailer(): LiveData<List<Trailer>> {
        return movieTrailer
    }

}