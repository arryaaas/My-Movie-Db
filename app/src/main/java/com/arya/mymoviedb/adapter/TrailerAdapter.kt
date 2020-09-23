package com.arya.mymoviedb.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arya.mymoviedb.BuildConfig.URL_THUMBNAIL
import com.arya.mymoviedb.BuildConfig.URL_TRAILER
import com.arya.mymoviedb.R
import com.arya.mymoviedb.model.Trailer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class TrailerAdapter(
    private val trailer: MutableList<Trailer>
): RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrailerAdapter.TrailerViewHolder {
        return TrailerViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.trailer,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return trailer.size
    }

    override fun onBindViewHolder(holder: TrailerAdapter.TrailerViewHolder, position: Int) {
        holder.bind(trailer[holder.adapterPosition])
    }

    inner class TrailerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(trailer: Trailer) {
            with(itemView) {
                val thumbnail = findViewById<ImageView>(R.id.thumbnail)
                Glide.with(itemView)
                    .load(URL_THUMBNAIL + trailer.key + "/maxresdefault.jpg")
                    .transform(CenterCrop())
                    .into(thumbnail)

                val name = findViewById<TextView>(R.id.name)
                name.text = trailer.name

                itemView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL_TRAILER + trailer.key))
                    context.startActivity(intent)
                }
            }
        }
    }
}