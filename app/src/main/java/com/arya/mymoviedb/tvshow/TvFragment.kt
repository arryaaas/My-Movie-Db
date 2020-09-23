package com.arya.mymoviedb.tvshow

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.arya.mymoviedb.api.DataSource
import com.arya.mymoviedb.api.NetworkProvider
import com.arya.mymoviedb.BuildConfig.API_KEY
import com.arya.mymoviedb.model.Genre
import com.arya.mymoviedb.R
import com.arya.mymoviedb.adapter.TvAdapter
import com.arya.mymoviedb.model.TvResponse
import com.arya.mymoviedb.model.TvResult
import com.arya.mymoviedb.viewmodel.TvViewModel
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tv.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class TvFragment : Fragment() {

    lateinit var viewSearch: SearchView
    private lateinit var tvAdapter: TvAdapter
    private lateinit var viewModel: TvViewModel

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
        return inflater.inflate(R.layout.fragment_tv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(TvViewModel::class.java)

        getAllTv()
        getAllGenres()
    }

    private fun getAllTv() {
        viewModel.setTv()
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
                        shimmerFrameLayoutTv.visibility = View.VISIBLE
                        getAllTv()
                        Alerter.hide()
                        showLoading(true)
                    }).show()
            }
        })

        viewModel.getTv().observe(viewLifecycleOwner, Observer { tvResult ->
            if (tvResult.isNotEmpty()) {
                shimmerFrameLayoutTv?.stopShimmerAnimation()
                tvAdapter =
                    TvAdapter(tvResult.toMutableList()) {
                        showTvDetail(it)
                    }
                rv_tv.adapter = tvAdapter
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
            shimmerFrameLayoutTv?.visibility = View.VISIBLE
            rv_tv?.visibility = View.GONE
        } else {
            shimmerFrameLayoutTv?.visibility = View.GONE
            rv_tv?.visibility = View.VISIBLE
        }
    }

    private fun showTvDetail(tvResult: TvResult) {
        val intent = Intent(context, TvDetailActivity::class.java).apply {
            putExtra(TvResult::class.java.simpleName, tvResult)
        }
        val options = ActivityOptions.makeCustomAnimation(context, R.anim.enter_from_right, R.anim.exit_to_left)
        startActivity(intent, options.toBundle())
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayoutTv.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerFrameLayoutTv.stopShimmerAnimation()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.search_menu)
        viewSearch = menuItem.actionView as SearchView
        viewSearch.queryHint = "Search tv shows..."
        setViewSearch()
    }

    private fun setViewSearch() {
        viewSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchTv(query)
                viewSearch.clearFocus()
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isEmpty()) {
                    rv_tv.visibility = View.VISIBLE
                    rv_search_tv.visibility = View.GONE
                    activity?.bottomNavigationView?.visibility = View.VISIBLE
                }
                return false
            }

        })

    }

    private fun searchTv(title: String) {

        rv_tv.visibility = View.GONE
        rv_search_tv.visibility = View.GONE
        pb_search_tv.visibility = View.VISIBLE
        activity?.bottomNavigationView?.visibility = View.GONE

        val dataSource = NetworkProvider.providesHttpAdapter()
            .create(DataSource::class.java)
        dataSource.searchTv(API_KEY, title).enqueue(object: Callback<TvResponse> {
            override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                Toast.makeText(context, "No result for query", Toast.LENGTH_LONG).show()
            }

            @Suppress("NAME_SHADOWING")
            override fun onResponse(
                call: Call<TvResponse>,
                response: Response<TvResponse>
            ) {
                if (response.isSuccessful) {

                    pb_search_tv.visibility = View.GONE

                    val tvResult = response.body()?.result
                    val rvSearchTv = view?.findViewById<RecyclerView>(R.id.rv_search_tv)

                    rvSearchTv?.visibility = View.VISIBLE

                    tvAdapter = TvAdapter(
                        tvResult!!.toMutableList()
                    ) { tvResult -> showTvDetail(tvResult) }
                    rvSearchTv?.adapter = tvAdapter
                }
            }

        })
    }

}
