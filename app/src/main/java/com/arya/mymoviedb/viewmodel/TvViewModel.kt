package com.arya.mymoviedb.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.mymoviedb.model.Genre
import com.arya.mymoviedb.model.GenresResponse
import com.arya.mymoviedb.api.DataSource
import com.arya.mymoviedb.api.NetworkProvider
import com.arya.mymoviedb.model.TvResponse
import com.arya.mymoviedb.model.TvResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvViewModel : ViewModel() {
    private val tv = MutableLiveData<List<TvResult>>()
    private val genre = MutableLiveData<List<Genre>>()
    private val error = MutableLiveData<Boolean>()

    fun setTv() {
        val dataSource = NetworkProvider.providesHttpAdapter()
            .create(DataSource::class.java)
        dataSource.discoverTv().enqueue(object : Callback<TvResponse> {
            override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                Log.e("TvViewModel", "${t.printStackTrace()}")
                error.value = true
            }

            override fun onResponse(call: Call<TvResponse>, response: Response<TvResponse>) {
                tv.value = response.body()?.result ?: emptyList()
            }

        })
    }

    fun setGenre() {
        val dataSource = NetworkProvider.providesHttpAdapter()
            .create(DataSource::class.java)
        dataSource.genreTv().enqueue(object : Callback<GenresResponse> {
            override fun onFailure(call: Call<GenresResponse>, t: Throwable) {
                Log.e("TvViewModel", "${t.printStackTrace()}")
            }

            override fun onResponse(
                call: Call<GenresResponse>,
                response: Response<GenresResponse>
            ) {
                genre.value = response.body()?.genres ?: emptyList()
            }

        })
    }

    fun getTv(): LiveData<List<TvResult>> {
        return tv
    }

    fun getGenre(): LiveData<List<Genre>> {
        return genre
    }

    fun getError(): LiveData<Boolean> {
        return error
    }
}