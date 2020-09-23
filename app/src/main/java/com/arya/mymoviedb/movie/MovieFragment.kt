package com.arya.mymoviedb.movie

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arya.mymoviedb.BuildConfig.API_KEY
import com.arya.mymoviedb.model.Genre
import com.arya.mymoviedb.R
import com.arya.mymoviedb.adapter.MovieAdapter
import com.arya.mymoviedb.api.DataSource
import com.arya.mymoviedb.api.NetworkProvider
import com.arya.mymoviedb.model.MovieResponse
import com.arya.mymoviedb.model.MovieResult
import com.arya.mymoviedb.viewmodel.MovieViewModel
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_movie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    lateinit var viewSearch: SearchView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: MovieViewModel

    companion object {
        var allGenres = listOf<Genre>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MovieViewModel::class.java)

        getAllMovies()
        getAllGenres()
    }

    private fun getAllMovies() {
        viewModel.setMovie()
        showLoading(true)

        viewModel.getError().observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                showLoading(false)

                Alerter.create(activity)
                    .setTitle("connection error")
                    .setText("make sure you are connected to the internet")
                    .setBackgroundColorRes(R.color.lightGreen)
                    .setIcon(R.drawable.ic_wifi)
                    .enableInfiniteDuration(true)
                    .addButton("Refresh", R.style.AlertButton, View.OnClickListener {
                        shimmerFrameLayout.visibility = View.VISIBLE
                        getAllMovies()
                        Alerter.hide()
                        showLoading(true)
                    }).show()
            }
        })

        viewModel.getMovie().observe(viewLifecycleOwner, Observer { movieResult ->
            if (movieResult.isNotEmpty()) {
                shimmerFrameLayout?.stopShimmerAnimation()
                movieAdapter =
                    MovieAdapter(movieResult.toMutableList()) {
                        showMovieDetail(it)
                    }
                rv_movie.adapter = movieAdapter
                showLoading(false)
            }
        })
    }

    private fun getAllGenres() {
        viewModel.setGenre()

        viewModel.getGenre().observe(viewLifecycleOwner, Observer { genre ->
            if (genre.isNotEmpty()) {
                allGenres = genre
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            shimmerFrameLayout?.visibility = View.VISIBLE
            rv_movie?.visibility = View.GONE
        } else {
            shimmerFrameLayout?.visibility = View.GONE
            rv_movie?.visibility = View.VISIBLE
        }
    }

    private fun showMovieDetail(movieResult: MovieResult) {
        val intent = Intent(context, MovieDetailActivity::class.java).apply {
            putExtra(MovieResult::class.java.simpleName, movieResult)
        }
        val options = ActivityOptions.makeCustomAnimation(context, R.anim.enter_from_right, R.anim.exit_to_left)
        startActivity(intent, options.toBundle())
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerFrameLayout.stopShimmerAnimation()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.search_menu)
        viewSearch = menuItem.actionView as SearchView
        viewSearch.queryHint = "Search movies..."
        setViewSearch()
    }

    private fun setViewSearch() {
        viewSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchMovie(query)
                viewSearch.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isEmpty()) {
                    rv_movie.visibility = View.VISIBLE
                    rv_search_movie.visibility = View.GONE
                    activity?.bottomNavigationView?.visibility = View.VISIBLE
                }
                return false
            }

        })
    }

    private fun searchMovie(title: String) {

        rv_movie.visibility = View.GONE
        rv_search_movie.visibility = View.GONE
        pb_search_movie.visibility = View.VISIBLE
        activity?.bottomNavigationView?.visibility = View.GONE

        val dataSource = NetworkProvider.providesHttpAdapter()
            .create(DataSource::class.java)
        dataSource.searchMovie(API_KEY, title).enqueue(object: Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(context, "No result for query", Toast.LENGTH_LONG).show()
            }

            @Suppress("NAME_SHADOWING")
            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                if (response.isSuccessful) {

                    pb_search_movie.visibility = View.GONE

                    val movieResult = response.body()?.result

                    rv_search_movie.visibility = View.VISIBLE

                    movieAdapter = MovieAdapter(
                        movieResult!!.toMutableList()
                    ) { movieResult -> showMovieDetail(movieResult) }
                    rv_search_movie?.adapter = movieAdapter
                }
            }

        })

    }

}
