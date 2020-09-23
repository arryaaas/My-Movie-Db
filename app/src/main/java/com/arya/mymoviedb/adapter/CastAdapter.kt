package com.arya.mymoviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arya.mymoviedb.BuildConfig.URL_CAST
import com.arya.mymoviedb.R
import com.arya.mymoviedb.model.Cast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class CastAdapter(
    private val cast: MutableList<Cast>
): RecyclerView.Adapter<CastAdapter.CastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.cast,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(cast[holder.adapterPosition])
    }

    inner class CastViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(cast: Cast) {
            with(itemView) {
                val name = findViewById<TextView>(R.id.name)
                name.text = cast.name

                val profilePath = findViewById<ImageView>(R.id.profile_path)
                Glide.with(itemView)
                    .load(URL_CAST + cast.profilePath)
                    .transform(CenterCrop())
                    .into(profilePath)
            }
        }
    }
}