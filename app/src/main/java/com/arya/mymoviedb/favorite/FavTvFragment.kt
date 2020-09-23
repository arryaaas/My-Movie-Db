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
import com.arya.mymoviedb.database.FavTvDB
import com.arya.mymoviedb.R
import com.arya.mymoviedb.adapter.TvAdapter

import com.arya.mymoviedb.tvshow.TvDetailActivity
import com.arya.mymoviedb.model.TvResult
import kotlinx.android.synthetic.main.fragment_fav_movie.*
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavTvFragment : BaseFragment() {

    private var tvAdapter: TvAdapter? = null

    @Suppress("NAME_SHADOWING")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_tv, container, false)
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
            this@FavTvFragment.let {
                val tvResult = FavTvDB(it.context!!.applicationContext).favTvDAO().readTv()
                val rvTv = view?.findViewById<RecyclerView>(R.id.rv_tv_fav)
                tvAdapter = TvAdapter(
                    tvResult.toMutableList()
                ) { _tvResult -> showTvDetail(_tvResult) }
                rvTv?.adapter = tvAdapter

                if (tvResult.isEmpty()) {
                    tv_noFav?.visibility = View.VISIBLE
                } else {
                    tv_noFav?.visibility = View.GONE
                    rvTv?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showTvDetail(tvResult: TvResult) {
        val intent = Intent(context, TvDetailActivity::class.java).apply {
            putExtra(TvResult::class.java.simpleName, tvResult)
            putExtra(TvDetailActivity.TYPE, "Favorite")
        }
        val options = ActivityOptions.makeCustomAnimation(context, R.anim.enter_from_right, R.anim.exit_to_left)
        startActivity(intent, options.toBundle())
    }
}
