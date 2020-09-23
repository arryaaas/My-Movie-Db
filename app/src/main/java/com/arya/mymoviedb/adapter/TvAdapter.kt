package com.arya.mymoviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.arya.mymoviedb.BuildConfig.URL_POSTER
import com.arya.mymoviedb.helper.DateTime
import com.arya.mymoviedb.R
import com.arya.mymoviedb.model.TvResult
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class TvAdapter (
    private val tvResults: MutableList<TvResult>,
    private val onTvClick: (tvResults: TvResult) -> Unit
): RecyclerView.Adapter<TvAdapter.TvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvViewHolder {
        return TvViewHolder(
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
        return tvResults.count()
    }

    override fun onBindViewHolder(holder: TvViewHolder, position: Int) {
        holder.bind(tvResults[holder.adapterPosition])
    }

    inner class TvViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(tvResult: TvResult) {
            with(itemView) {
                val title = findViewById<TextView>(R.id.tv_title)
                title.text = tvResult.name//.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

                val release = findViewById<TextView>(R.id.tv_release_date)
                release.text = DateTime.getLongDate(tvResult.firstairdate.toString())

                val rating = findViewById<RatingBar>(R.id.ratingBar)
                rating.rating = tvResult.voteAverage?.div(2F) ?: 0F

                val vote = findViewById<TextView>(R.id.tv_rating)
                vote.text = tvResult.voteAverage.toString()

                val posterPath = findViewById<ImageView>(R.id.iv_poster_img)
                Glide.with(itemView)
                    .load(URL_POSTER + tvResult.posterPath)
//                    .apply(RequestOptions())
                    .transform(CenterCrop())
                    .into(posterPath)

                itemView.setOnClickListener {
                    onTvClick.invoke(tvResult)
                }
            }
        }
    }
}