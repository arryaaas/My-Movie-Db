package com.arya.mymoviedb.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.mymoviedb.api.DataSource
import com.arya.mymoviedb.api.NetworkProvider
import com.arya.mymoviedb.model.Cast
import com.arya.mymoviedb.model.CastResponse
import com.arya.mymoviedb.model.Trailer
import com.arya.mymoviedb.model.TrailerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTvViewModel : ViewModel() {
    private val tvCast = MutableLiveData<List<Cast>>()
    private val tvTrailer = MutableLiveData<List<Trailer>>()

    fun setCast(tvId: Int) {
        val dataSource = NetworkProvider.providesHttpAdapter()
            .create(DataSource::class.java)
        dataSource.getTvCredits(tvId).enqueue(object : Callback<CastResponse> {
            override fun onFailure(call: Call<CastResponse>, t: Throwable) {
                Log.e("CastTvViewModel", "${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<CastResponse>, response: Response<CastResponse>) {
                tvCast.value = response.body()?.cast ?: emptyList()
            }

        })
    }

    fun setTrailer(tvId: Int) {
        val dataSource = NetworkProvider.providesHttpAdapter()
            .create(DataSource::class.java)
        dataSource.getTvTrailer(tvId).enqueue(object : Callback<TrailerResponse> {
            override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                Log.e("TrailerTvViewModel", "${t.printStackTrace()}")
            }

            override fun onResponse(
                call: Call<TrailerResponse>,
                response: Response<TrailerResponse>
            ) {
                tvTrailer.value = response.body()?.result ?: emptyList()
            }

        })
    }

    fun getCast(): LiveData<List<Cast>> {
        return tvCast
    }

    fun getTrailer(): LiveData<List<Trailer>> {
        return tvTrailer
    }
}