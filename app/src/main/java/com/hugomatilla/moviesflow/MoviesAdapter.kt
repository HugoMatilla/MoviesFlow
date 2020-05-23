package com.hugomatilla.moviesflow;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.movie_item.view.*

class MoviesAdapter(private val itemClick: (Movie) -> Unit) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var movies = emptyList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindArticle(movies.elementAt(position))
    }

    override fun getItemCount() = movies.size

    internal fun setItems(items: List<Movie>) {
        this.movies = items
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, private val itemClick: (Movie) -> Unit) :
        RecyclerView.ViewHolder(view) {
        fun bindArticle(movie: Movie) {
            with(movie) {
                itemView.title.text = title
                itemView.star.visibility = if (starred) View.VISIBLE else View.INVISIBLE
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}