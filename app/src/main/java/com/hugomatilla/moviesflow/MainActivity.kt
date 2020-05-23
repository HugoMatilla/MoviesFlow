package com.hugomatilla.moviesflow

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val viewModel: MoviesViewModel by viewModels()
    private val movieAdapter by lazy { MoviesAdapter { starMovie(it) } }

    private fun starMovie(movie: Movie) {
        launch {
            dao.starMovieById(movie.id, !movie.starred)
        }

    }

    private val service by lazy { MovieCloudServiceImpl().create() }
    private val dao by lazy { DB.getInstance().movieDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DB.init(this)

        viewModel.data.observeForever { movies ->
            (list.adapter as MoviesAdapter).setItems(movies)
            movies.also { println("🚛 $it") }
        }
        initAdapter()

    }

    private fun initAdapter() {
        list.adapter = movieAdapter
        list.layoutManager = LinearLayoutManager(this)
        swipeToRefresh.setOnRefreshListener { getDataFromCloud() }
    }

    private fun getDataFromCloud() {
        swipeToRefresh.isRefreshing = true
        launch {
            val result = service.getMovies(API_KEY)
            dao.insertAll(result.movies)
            withContext(Dispatchers.IO) {
                swipeToRefresh.isRefreshing = false
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}
