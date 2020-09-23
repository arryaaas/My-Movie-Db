package com.arya.mymoviedb.tvshow

import android.animation.LayoutTransition
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arya.mymoviedb.helper.BaseActivity
import com.arya.mymoviedb.BuildConfig.URL_BACKDROP
import com.arya.mymoviedb.BuildConfig.URL_POSTER
import com.arya.mymoviedb.adapter.CastAdapter
import com.arya.mymoviedb.database.FavTvDB
import com.arya.mymoviedb.helper.DateTime
import com.arya.mymoviedb.R
import com.arya.mymoviedb.adapter.TrailerAdapter
import com.arya.mymoviedb.database.SharedPref
import com.arya.mymoviedb.model.Cast
import com.arya.mymoviedb.model.Trailer
import com.arya.mymoviedb.model.TvResult
import com.arya.mymoviedb.viewmodel.DetailTvViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_tv_detail.*
import kotlinx.android.synthetic.main.activity_tv_detail.iv_backdrop
import kotlinx.android.synthetic.main.activity_tv_detail.iv_poster
import kotlinx.android.synthetic.main.activity_tv_detail.parent_layout
import kotlinx.android.synthetic.main.activity_tv_detail.ratingBar
import kotlinx.android.synthetic.main.activity_tv_detail.rv_cast
import kotlinx.android.synthetic.main.activity_tv_detail.rv_trailer
import kotlinx.android.synthetic.main.activity_tv_detail.tv_genres
import kotlinx.android.synthetic.main.activity_tv_detail.tv_overview
import kotlinx.android.synthetic.main.activity_tv_detail.tv_rating
import kotlinx.android.synthetic.main.activity_tv_detail.tv_release_date
import kotlinx.android.synthetic.main.activity_tv_detail.tv_title
import kotlinx.coroutines.launch

class TvDetailActivity : BaseActivity() {

    private lateinit var sharedPref: SharedPref
    private lateinit var castAdapter: CastAdapter
    private lateinit var trailerAdapter: TrailerAdapter
    private lateinit var viewModel: DetailTvViewModel

    var id = 0
    private var type = ""

    private val maxLinesCollapsed = 2
    private val initialIsCollapsed = true
    private var isCollapsed = initialIsCollapsed

    companion object {
        const val TYPE = "extra_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        updateTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_detail)

        setSupportActionBar(findViewById(R.id.toolbar_detail))
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = ""
        }

        type = intent.extras?.getString(TYPE, "") ?: ""
        val extras = intent.getParcelableExtra(TvResult::class.java.simpleName) ?: TvResult()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailTvViewModel::class.java)

        setUi(extras)
        applyLayoutTransition()
    }

    private fun setUi(tvResult: TvResult) {

        id = tvResult.id
        val name = tvResult.name
        val firstAirDate = tvResult.firstairdate
        val overview = tvResult.overview
        val poster = tvResult.posterPath
        val backdrop = tvResult.backdropPath
        val rating = tvResult.voteAverage?.div(2F)
        val vote = tvResult.voteAverage
        val genreIds = tvResult.genreIds ?: emptyList()
        val listGenre: List<String>
        var listCast = listOf<Cast>()
        var listTrailer = listOf<Trailer>()

        if (type == "Favorite") {
            listGenre = tvResult.genre?.toList() ?: emptyList()
            listCast = tvResult.cast?.toList() ?: emptyList()
            castAdapter = CastAdapter(listCast.toMutableList())
            rv_cast.adapter = castAdapter
            listTrailer = tvResult.trailer?.toList() ?: emptyList()
            trailerAdapter = TrailerAdapter(listTrailer.toMutableList())
            rv_trailer.adapter = trailerAdapter
        } else {
            listGenre = getGenre(genreIds)
            viewModel.setCast(id)
            viewModel.getCast().observe(this, Observer { cast ->
                if (cast.isNotEmpty()) {
                    castAdapter = CastAdapter(cast.toMutableList())
                    rv_cast.adapter = castAdapter
                    listCast = cast
                }
            })
            viewModel.setTrailer(id)
            viewModel.getTrailer().observe(this, Observer { trailer ->
                if (trailer.isNotEmpty()) {
                    trailerAdapter = TrailerAdapter(trailer.toMutableList())
                    rv_trailer.adapter = trailerAdapter
                    listTrailer = trailer
                }
            })
        }

        backdrop?.let { backdropPath ->
            Glide.with(this)
                .load(URL_BACKDROP + backdropPath)
                .transform(CenterCrop())
                .into(iv_backdrop)
        }
        poster?.let { posterPath ->
            Glide.with(this)
                .load(URL_POSTER + posterPath)
                .transform(CenterCrop())
                .into(iv_poster)
        }
        tv_title.text = name
        tv_release_date.text = DateTime.getLongDate(firstAirDate.toString())
        ratingBar.rating = rating ?: 0F
        tv_rating.text = vote.toString()
        tv_genres.text = TextUtils.join(", ", listGenre)
        tv_overview.text = overview
        tv_overview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_arrow_down)

        tv_overview.setOnClickListener {
            if (isCollapsed) {
                tv_overview.maxLines = Int.MAX_VALUE
                tv_overview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            } else {
                tv_overview.maxLines = maxLinesCollapsed
                tv_overview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_arrow_down)
            }
            isCollapsed = !isCollapsed
        }

        launch {
            this@TvDetailActivity.let {
                val isFavorite = FavTvDB(it).favTvDAO().isFavoriteTv(id)
                if (isFavorite == 1) {
                    btnFavTv.setText(R.string.btn_remove_favorite)
                    btnFavTv.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#b71c1c"))
                } else {
                    btnFavTv.setText(R.string.btn_add_favorite)
                    btnFavTv.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#24D6AA"))
                }
            }
        }

        btnFavTv.setOnClickListener {
            val fav = TvResult()
            fav.id = id
            fav.name = name
            fav.firstairdate = firstAirDate
            fav.overview = overview
            fav.posterPath = poster
            fav.backdropPath = backdrop
            fav.voteAverage = vote
            fav.genre = listGenre
            fav.cast = listCast
            fav.trailer = listTrailer

            launch {
                this@TvDetailActivity.let {
                    val isFavorite = FavTvDB(it).favTvDAO().isFavoriteTv(id)
                    if (isFavorite != 1) {
                        FavTvDB(it).favTvDAO().saveTv(fav)
                        btnFavTv.setText(R.string.btn_remove_favorite)
                        btnFavTv.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#b71c1c"))
                        Alerter.create(this@TvDetailActivity)
                            .setText(R.string.toast_add_favorite)
                            .setBackgroundColorRes(R.color.lightGreen) // or setBackgroundColorInt(Color.CYAN)
                            .show()
                    } else {
                        FavTvDB(it).favTvDAO().delTv(fav)
                        btnFavTv.setText(R.string.btn_add_favorite)
                        btnFavTv.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#24D6AA"))
                        Alerter.create(this@TvDetailActivity)
                            .setText(R.string.toast_remove_favorite)
                            .setBackgroundColorRes(R.color.red) // or setBackgroundColorInt(Color.CYAN)
                            .show()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_menu) {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/tv/$id")
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        } else {
            return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun updateTheme() {
        sharedPref = SharedPref(this)
        if (sharedPref.loadNightModeState() == true) {
            setTheme(R.style.darktheme)
        } else {
            setTheme(R.style.AppTheme)
        }
    }

    private fun getGenre(id: List<Int>): ArrayList<String> {
        val allGenres = TvFragment.allGenres
        val genres = arrayListOf<String>()
        if (id.isNotEmpty()) {
            for (i in id.indices) {
                for (j in allGenres.indices) {
                    if (id[i] == allGenres[j].id) {
                        genres.add(allGenres[j].name ?: "")
                        break
                    }
                }
            }
        }
        return genres
    }

    private fun applyLayoutTransition() {
        val transition = LayoutTransition()
        transition.setDuration(300)
        transition.enableTransitionType(LayoutTransition.CHANGING)
        parent_layout.layoutTransition = transition
    }
}
