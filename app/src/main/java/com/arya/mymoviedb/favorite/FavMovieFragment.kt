package com.arya.mymoviedb.favorite

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arya.mymoviedb.helper.BaseFragment
import com.arya.mymoviedb.database.FavMovieDB
import com.arya.mymoviedb.adapter.MovieAdapter
import com.arya.mymoviedb.movie.MovieDetailActivity
import com.arya.mymoviedb.model.MovieResult

import com.arya.mymoviedb.R
import kotlinx.android.synthetic.main.fragment_fav_movie.*
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavMovieFragment : BaseFragment() {

    private var movieAdapter: MovieAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {

        launch {
            this@FavMovieFragment.let {
                val movieResult = FavMovieDB(it.context!!.applicationContext).favMovieDAO().readMovie()
                val rvMovie = view?.findViewById<RecyclerView>(R.id.rv_movie_fav)
                movieAdapter = MovieAdapter(
                    movieResult.toMutableList()
                ) { _movieResult -> showMovieDetail(_movieResult) }
                rvMovie?.adapter = movieAdapter

                if (movieResult.isEmpty()) {
                    tv_noFav?.visibility = View.VISIBLE
                } else {
                    tv_noFav?.visibility = View.GONE
                    rvMovie?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showMovieDetail(movieResult: MovieResult) {
        val intent = Intent(context, MovieDetailActivity::class.java).apply {
            putExtra(MovieResult::class.java.simpleName, movieResult)
            putExtra(MovieDetailActivity.TYPE, "Favorite")
        }
        val options = ActivityOptions.makeCustomAnimation(context, R.anim.enter_from_right, R.anim.exit_to_left)
        startActivity(intent, options.toBundle())
    }


}
