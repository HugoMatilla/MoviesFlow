package com.hugomatilla.moviesflow.home.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.hugomatilla.moviesflow.R
import com.hugomatilla.moviesflow.data.cloud.API_KEY
import com.hugomatilla.moviesflow.data.cloud.MovieApiService
import com.hugomatilla.moviesflow.data.db.AppDB
import com.hugomatilla.moviesflow.data.db.Movie
import com.hugomatilla.moviesflow.data.local.LocalCache
import com.hugomatilla.moviesflow.detail.MovieDetailActivity
import com.hugomatilla.moviesflow.random_movie.RandomMovieActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val viewModel: MoviesViewModel by viewModel()
    private val service: MovieApiService by inject()
    private val cache: LocalCache by inject()

    private val movieAdapter by lazy { MoviesAdapter({ startDetail(it) }, { starMovie(it) }) }
    private val movieAdapter2 by lazy { MoviesAdapter({}, { true }) }
    private val movieAdapter3 by lazy { MoviesAdapter({}, { true }) }

    private fun startDetail(movie: Movie) {
        MovieDetailActivity.start(this, movie.id)
    }

    private fun starMovie(movie: Movie): Boolean {
        launch { dao.starMovieById(movie.id, !movie.starred) }
        return true // Returns true for the LongClick
    }

    private val dao by lazy { AppDB.getInstance().movieDao() }

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        initAdapter()
        setUpViewModel()
        getDataFromCloud()
        goToRandom.setOnClickListener { RandomMovieActivity.start(this@MainActivity) }
    }

    private fun setUpViewModel() {
        viewModel.allMovies?.observeForever { movies ->
            (list2.adapter as MoviesAdapter).setItems(movies.sortedBy { it.title })
            (list3.adapter as MoviesAdapter).setItems(movies.sortedBy { it.rating })
        }
        viewModel.newMovies.observeForever { movies ->
            (list.adapter as MoviesAdapter).setItems(movies.sortedBy { it.release_date })
            list1Title.text = "New"
        }
    }

    private fun setDarkMode(isDark: Boolean = true) {
        cache.isDarkMode = isDark
        AppCompatDelegate.setDefaultNightMode(if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (isDark) window.navigationBarColor = Color.BLACK
    }

    private fun initAdapter() {
        list.adapter = movieAdapter
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        list2.adapter = movieAdapter2
        list2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        list3.adapter = movieAdapter3
        list3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        swipeToRefresh.setOnRefreshListener { getDataFromCloud() }
    }

    private fun setupToolbar() {
        setDarkMode(cache.isDarkMode)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_theme_mode -> setDarkMode(resources.getString(R.string.theme_type) != "dark")
            }
            true
        }
    }


    private fun getDataFromCloud() {
        swipeToRefresh.isRefreshing = true
        launch {
            val result = service.getMovies(apiKey = API_KEY)
            dao.insertAll(result.movies)
            withContext(Dispatchers.Main) {
                swipeToRefresh.isRefreshing = false
            }
        }
        viewModel.getNewMovies()
        list1Title.text = "New..."
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}
