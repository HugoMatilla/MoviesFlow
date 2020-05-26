package com.hugomatilla.moviesflow.home.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.hugomatilla.moviesflow.R
import com.hugomatilla.moviesflow.data.cloud.IMAGE_BASE_URL
import com.hugomatilla.moviesflow.data.db.Movie
import kotlinx.android.synthetic.main.movie_item.view.*

class MoviesAdapter(
    private val itemClick: (Movie) -> Unit,
    private val itemLongClick: (Movie) -> Boolean
) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var movies = emptyList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(
            view,
            itemClick,
            itemLongClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindArticle(movies.elementAt(position))
    }

    override fun getItemCount() = movies.size

    internal fun setItems(items: List<Movie>) {
        this.movies = items
        notifyDataSetChanged()
    }

    class ViewHolder(
        view: View,
        private val itemClick: (Movie) -> Unit,
        private val itemLongClick: (Movie) -> Boolean
    ) :
        RecyclerView.ViewHolder(view) {
        fun bindArticle(movie: Movie) {
            with(movie) {
                itemView.title.text = title
                itemView.star.visibility = if (starred) View.VISIBLE else View.INVISIBLE
                itemView.setOnClickListener { itemClick(this) }
                itemView.setOnLongClickListener { itemLongClick(this) }
                itemView.image.load("$IMAGE_BASE_URL${movie.image}")
            }
        }
    }
}