package com.arya.mymoviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.arya.mymoviedb.BuildConfig.URL_POSTER
import com.arya.mymoviedb.helper.DateTime
import com.arya.mymoviedb.R
import com.arya.mymoviedb.model.MovieResult
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class MovieAdapter(
    private val movieResults: MutableList<MovieResult>,
    private val onMovieClick: (movieResults: MovieResult) -> Unit
): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return movieResults.count()
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieResults[holder.adapterPosition])
    }

    inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(movieResults: MovieResult) {
            with(itemView) {
                val title = findViewById<TextView>(R.id.tv_title)
                title.text = movieResults.title//.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

                val release = findViewById<TextView>(R.id.tv_release_date)
                release.text = DateTime.getLongDate(movieResults.releaseDate.toString())

                val rating = findViewById<RatingBar>(R.id.ratingBar)
                rating.rating = movieResults.voteAverage?.div(2F) ?: 0F

                val vote = findViewById<TextView>(R.id.tv_rating)
                vote.text = movieResults.voteAverage.toString()

                val posterPath = findViewById<ImageView>(R.id.iv_poster_img)
                Glide.with(itemView)
                    .load(URL_POSTER + movieResults.posterPath)
//                    .apply(RequestOptions())
                    .transform(CenterCrop())
                    .into(posterPath)

                itemView.setOnClickListener {
                    onMovieClick.invoke(movieResults)
                }
            }
        }
    }
}