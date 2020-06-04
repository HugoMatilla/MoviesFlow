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

        AppDB.init(this)

        viewModel.data.observeForever { movies ->
            (list.adapter as MoviesAdapter).setItems(movies.sortedBy { it.release_date })
            (list2.adapter as MoviesAdapter).setItems(movies.sortedBy { it.title })
            (list3.adapter as MoviesAdapter).setItems(movies.sortedBy { it.rating })
        }

        initAdapter()
        getDataFromCloud()

        goToRandom.setOnClickListener { RandomMovieActivity.start(this@MainActivity) }

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
