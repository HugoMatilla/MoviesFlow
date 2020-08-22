package com.hugomatilla.moviesflow.random_movie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.hugomatilla.moviesflow.R
import kotlinx.android.synthetic.main.activity_random_movie.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

class RandomMovieActivity : AppCompatActivity() {
    companion object {
        fun start(caller: Activity) {
            caller.startActivity(Intent(caller, this::class.java.declaringClass))
        }
    }

    private val viewModel: RandomMovieViewModel by viewModel()

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_movie)
        viewModel.data.observe(this) {
            movieTitle.text = it.toString()
            swipe.isRefreshing = false
        }
        reload.setOnClickListener { viewModel.getNewMovie() }
        swipe.setOnRefreshListener { getNewMovie() }
    }

    private fun getNewMovie() {
        swipe.isRefreshing = true
        viewModel.getNewMovie()
    }
}

fun <T> LiveData<T>.observe(owner: LifecycleOwner, f: (T?) -> Unit) {
    this.observe(owner, Observer<T> { t -> f(t) })
}
