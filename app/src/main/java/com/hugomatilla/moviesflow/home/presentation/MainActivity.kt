package com.hugomatilla.moviesflow.home.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.motion.widget.MotionLayout
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

    private val movieAdapter0 by lazy { MoviesAdapter({ startDetail(it) }, { starMovie(it) }) }
    private val movieAdapter1 by lazy { MoviesAdapter({ startDetail(it) }, { starMovie(it) }) }
    private val movieAdapter2 by lazy { MoviesAdapter({ startDetail(it) }, { starMovie(it) }) }
    private val movieAdapter3 by lazy { MoviesAdapter({ startDetail(it) }, { starMovie(it) }) }

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
        goToRandom.setOnClickListener {
            if (mainMotionLayout.progress == 0F) mainMotionLayout.transitionToEnd()

            mainMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                }

                override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                    if (p3 == 1.0F) RandomMovieActivity.start(this@MainActivity)
                }


                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                    p1.also { println("🚛 p1 $it") }

                }
            })

        }
    }

    private fun setUpViewModel() {
        viewModel.allMovies.observeForever { movies ->
            (list2.adapter as MoviesAdapter).setItems(movies.sortedBy { it.title })
            (list3.adapter as MoviesAdapter).setItems(movies.sortedBy { it.rating })
        }
        viewModel.newMovies.observeForever { movies ->
            movies.also { println("News 🚛 $it") }
            (list1.adapter as MoviesAdapter).setItems(movies.sortedBy { it.release_date })
            list1Title.text = "New"
        }
        viewModel.favMovies.observeForever { movies ->
            movies.also { println("Favs 🚛 $it") }
            (list0.adapter as MoviesAdapter).setItems(movies)
            list0.visibility = if (movies.isEmpty()) GONE else VISIBLE
            list0Title.visibility = list0.visibility
        }

    }

    private fun setDarkMode(isDark: Boolean = true) {
        cache.isDarkMode = isDark
        AppCompatDelegate.setDefaultNightMode(if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (isDark) window.navigationBarColor = Color.BLACK
    }

    private fun initAdapter() {
        list0.adapter = movieAdapter0
        list0.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        list1.adapter = movieAdapter1
        list1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

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
