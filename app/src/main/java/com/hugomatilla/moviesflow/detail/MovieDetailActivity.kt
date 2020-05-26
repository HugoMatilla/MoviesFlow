package com.hugomatilla.moviesflow.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hugomatilla.moviesflow.R
import com.hugomatilla.moviesflow.data.cloud.API_KEY
import com.hugomatilla.moviesflow.data.cloud.MovieCloudServiceImpl
import com.hugomatilla.moviesflow.data.db.AppDB
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MovieDetailActivity : AppCompatActivity(), CoroutineScope {
    companion object {
        const val ID = "ID"
        fun start(caller: Activity, id: Long) {
            val intent = Intent(caller, this::class.java.declaringClass)
            intent.putExtra(ID, id)
            caller.startActivity(intent)
        }
    }

    private val service by lazy { MovieCloudServiceImpl()
        .create() }
    private val dao by lazy { AppDB.getInstance().movieDao() }
    private val id by lazy { intent.getLongExtra(ID, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        getDataFromCloud()
    }

    private fun getDataFromCloud() {
        swipeToRefresh.isRefreshing = true
        launch {
            val movie = service.getMovie(id,
                API_KEY
            )
            withContext(Dispatchers.Main) {
                swipeToRefresh.isRefreshing = false
                overview.text = """
                    ${movie.title}
                    ${movie.overview}
                    ${movie.budget}
                """
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}