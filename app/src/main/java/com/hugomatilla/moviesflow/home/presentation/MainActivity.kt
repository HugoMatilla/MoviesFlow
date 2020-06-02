package com.hugomatilla.moviesflow.home.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hugomatilla.moviesflow.R
import com.hugomatilla.moviesflow.data.cloud.API_KEY
import com.hugomatilla.moviesflow.data.cloud.MovieApiService
import com.hugomatilla.moviesflow.data.db.AppDB
import com.hugomatilla.moviesflow.data.db.Movie
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

    private val movieAdapter by lazy { MoviesAdapter({ startDetail(it) }, { starMovie(it) }) }

    private fun startDetail(movie: Movie) {
        MovieDetailActivity.start(this, movie.id)
    }

    private fun starMovie(movie: Movie): Boolean {
        launch { dao.starMovieById(movie.id, !movie.starred) }
        return true // For the LongClick
    }

    private val dao by lazy { AppDB.getInstance().movieDao() }

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppDB.init(this)

        viewModel.data.observeForever { (list.adapter as MoviesAdapter).setItems(it) }

        initAdapter()
        getDataFromCloud()

        goToRandom.setOnClickListener { RandomMovieActivity.start(this@MainActivity) }

    }

    private fun initAdapter() {
        list.adapter = movieAdapter
        list.layoutManager = LinearLayoutManager(this)
        swipeToRefresh.setOnRefreshListener { getDataFromCloud() }
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
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}
